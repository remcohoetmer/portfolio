package nl.remco.group.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import nl.remco.group.enrich.OrganisationEnricher;
import nl.remco.group.enrich.PersonEnricher;
import nl.remco.group.enrich.ScopeEnricher;
import nl.remco.group.service.domain.Group;
import nl.remco.group.service.dto.GroupDTO;


@Service
public class MongoDbGroupService implements GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbGroupService.class);

	private final GroupRepository repository;
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	OrganisationEnricher organisationEnricher;

	@Autowired
	PersonEnricher personEnricher;

	@Autowired
	ScopeEnricher scopeEnricher;

	@Autowired
	Converter converter;

	@Autowired
	MongoDbGroupService(GroupRepository repository) {
		this.repository = repository;
	}

	// enrich the groups, controlled by the selection parameters
	// implementation: all enrichments are translated to asynchronous tasks.
	// these tasks are collected at the end, resulting in maximal parallelism
	private CompletableFuture<List<GroupDTO>> enrich( CompletableFuture<List<GroupDTO>> groups_cf,
			GroupFilter groupFilter, 
			GroupSelection groupSelection)
	{
		return groups_cf.thenCompose( groups-> {
			List<CompletableFuture<?>> list= new ArrayList<>();
			if (groupSelection.isSelectOrganisations())
				list.addAll( organisationEnricher.enrichOrganisations(groups));
			if (groupSelection.isSelectPersons())
				list.addAll( personEnricher.enrichPersons(groups));
			if (groupSelection.isSelectScopes())
				list.addAll( scopeEnricher.enrichScopes(groups));
			
			if (groupSelection.isSelectMasters()) {
				list.addAll( enrichMasters(groups, groupFilter));				
			}
			return CompletableFuture.allOf(list.stream().toArray(CompletableFuture[]::new))
					.thenApply( dummy-> groups);
		});
	}

	private List<CompletableFuture<?>> enrichMasters(List<GroupDTO> groups, GroupFilter groupFilter) {
		List<CompletableFuture<?>> list= new ArrayList<>();
		for (GroupDTO groupDTO: groups) {
			if ( groupDTO.getMaster()!= null
					&& groupDTO.getMaster().getId()!=null
					&& !groupDTO.getMaster().getId().isEmpty()) {
					list.add(
							findById( groupDTO.getMaster().getId())
							.thenApply(master -> { groupDTO.setMaster(master); return groupDTO;})
							.exceptionally( t-> { System.err.println(t); return groupDTO;}));
					
			}
		}
		return list;
	}

	@Override
	public CompletableFuture<GroupDTO> create(GroupDTO group) {
		LOGGER.info("Creating a new group entry with information: {}", group);

		Group groupDTO = converter.convertfromDTO(group);

		return repository.save(groupDTO)
				.thenApply( persisted-> {
					LOGGER.info("Created a new group entry with information: {}", persisted);

					return converter.convertToDTO(persisted);
				});
	}


	@Override
	public CompletableFuture<GroupDTO> delete(String id) {
		LOGGER.info("Deleting a group entry with id: {}", id);

		return findGroupById(id)
				.thenCompose( group-> {
					return repository.delete(group)
							.thenApply( dummy -> {
								LOGGER.info("Deleted group entry with informtation: {}", group);
								return converter.convertToDTO(group);
							});
				});
	}

	@Override
	public CompletableFuture<List<GroupDTO>> find(GroupFilter groupFilter, GroupSelection groupSelection) {
		LOGGER.info("Finding all group entries.");

		//		repository.findAll()

		CompletableFuture<List<GroupDTO>> groupEntries = findGroups( groupFilter)
				.thenApply(this::convertToDTOs);
		return enrich( groupEntries, groupFilter, groupSelection);
	}

	private CompletableFuture<List<Group>> findGroups(GroupFilter groupFilter) {
		Query query = new Query();
		if (!groupFilter.getName().isEmpty()) {
			query.addCriteria(Criteria.where("name").regex(groupFilter.getName()));
		}
		if (!groupFilter.getDescription().isEmpty()) {
			query.addCriteria(Criteria.where("description").regex(groupFilter.getDescription()));
		}
		if (!groupFilter.getStatus().isEmpty()) {
			query.addCriteria(Criteria.where("status").regex(groupFilter.getStatus()));
		}
		if (!groupFilter.getCode().isEmpty()) {
			query.addCriteria(Criteria.where("code").regex(groupFilter.getCode()));
		}
		if (!groupFilter.getMasterId().isEmpty()){
			query.addCriteria(Criteria.where("masterId").is(groupFilter.getMasterId()));
		}
		if (!groupFilter.getScopeId().isEmpty()){
			query.addCriteria(Criteria.where("scopeId").is(groupFilter.getScopeId()));
		}
		if (!groupFilter.getOrganisationId().isEmpty()){
			query.addCriteria(Criteria.where("organisationId").is(groupFilter.getOrganisationId()));
		}
		if (groupFilter.getFeatures()!=null && !groupFilter.getFeatures().isEmpty()){
			query.addCriteria(Criteria.where("features").in(groupFilter.getFeatures()));
		}
		//TODO:make async
		List<Group> scopeEntries= mongoTemplate.find(query, Group.class);

		return CompletableFuture.completedFuture(scopeEntries);
	}

	
	private List<GroupDTO> convertToDTOs(List<Group> models) {
		return models.stream()
				.map(converter::convertToDTO)
				.collect(toList());
	}

	@Override
	public CompletableFuture<GroupDTO> findById(String id) {
		LOGGER.info("Finding group entry with id: {}", id);

		return findGroupById(id)
				.thenApply( found -> {
					LOGGER.info("Found group entry: {}", found);
					return converter.convertToDTO(found);

				})
				.thenCompose( personEnricher::enrichPersons)
				.thenCompose( scopeEnricher::enrichScopes)
				.thenCompose( organisationEnricher::enrichOrganisations);
	}

	@Override
	public CompletableFuture<GroupDTO> update(GroupDTO group) {
		LOGGER.info("Updating group entry with information: {}", group);

		return findGroupById(group.getId())
				.thenApply( updated-> {
					updated.update(group.getStatus(), group.getName());
					return updated;
				})
				.thenCompose( repository::save)
				.thenApply( updated -> {
					LOGGER.info("Updated group entry with information: {}", updated);

					return converter.convertToDTO(updated);
				});
	}



	private CompletableFuture<Group> findGroupById(String id) {
		return repository.findOne(id);
	}
}
