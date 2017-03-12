package nl.remco.group.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bson.BSON;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.WriteResult;

import nl.remco.group.enrich.PersonEnricher;
import nl.remco.group.enrich.ScopeEnricher;
import nl.remco.group.organisation.service.OrganisationEnricher;
import nl.remco.group.service.domain.Membership;
import nl.remco.group.service.domain.Person;
import nl.remco.group.service.domain.RGroup;
import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;


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
						.exceptionally( t-> { t.printStackTrace(); return groupDTO;}));

			}
		}
		return list;
	}

	@Override
	public CompletableFuture<GroupDTO> create(GroupDTO group) {
		LOGGER.info("Creating a new group entry with information: {}", group);
		RGroup groupDTO = converter.convertfromDTO(group);
		return repository.save(groupDTO)
				.thenApply( converter::convertToDTO);
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

		CompletableFuture<List<GroupDTO>> groupEntries = findGroups( groupFilter)
				.thenApply(this::convertToDTOs);
		return enrich( groupEntries, groupFilter, groupSelection);
	}

	private CompletableFuture<List<RGroup>> findGroups(GroupFilter groupFilter) {
		Query query = new Query();
		if (groupFilter.getName()!=null) {
			query.addCriteria(Criteria.where("name").regex(".*" + groupFilter.getName() + ".*"));
		}
		if (groupFilter.getDescription()!=null) {
			query.addCriteria(Criteria.where("description").regex(groupFilter.getDescription()));
		}
		if (groupFilter.getStatus()!=null) {
			query.addCriteria(Criteria.where("status").is(groupFilter.getStatus()));
		}
		if (groupFilter.getCode()!=null) {
			query.addCriteria(Criteria.where("code").is(groupFilter.getCode()));
		}
		if (groupFilter.getPersonId()!=null) {
			query.addCriteria(Criteria.where("memberships.person.id").is(groupFilter.getPersonId()));
		}
		if (groupFilter.getMasterId()!=null){
			query.addCriteria(Criteria.where("master.id").is(groupFilter.getMasterId()));
		}
		if (groupFilter.getScopeId()!=null){
			query.addCriteria(Criteria.where("scope.id").is(new ObjectId(groupFilter.getScopeId())));
		}
		if (groupFilter.getOrganisationId()!=null){
			query.addCriteria(Criteria.where("organisation.id").is(groupFilter.getOrganisationId()));
		}
		if (groupFilter.getFeatures()!=null && !groupFilter.getFeatures().isEmpty()){
			query.addCriteria(Criteria.where("features").in(groupFilter.getFeatures()));
		}
		LOGGER.info( "Query" + new String(BSON.encode(query.getQueryObject())));
		List<RGroup> scopeEntries= mongoTemplate.find(query, RGroup.class);

		return CompletableFuture.completedFuture(scopeEntries);
	}


	private List<GroupDTO> convertToDTOs(List<RGroup> models) {
		return models.stream()
				.map(converter::convertToDTO)
				.collect(toList());
	}

	@Override
	public CompletableFuture<GroupDTO> findById(String id) {
		LOGGER.info("Finding group entry with id: {}", id);

		return findGroupById(id)
				.thenApply( found -> {// Found can be null!
					LOGGER.info("Found group entry: {}", found);
					return converter.convertToDTO(found);

				})
				.thenCompose( personEnricher::enrichPersons)
				.thenCompose( scopeEnricher::enrichScopes)
				.thenCompose( organisationEnricher::enrichOrganisations);
	}

	private Membership convertFromDTO( MembershipDTO membershipDTO)
	{
		Membership membership= new Membership();
		membership.setPerson(new Person( membershipDTO.getPerson().getId(), membershipDTO.getPerson().getName()));
		membership.setRole(membershipDTO.getRole());
		return membership;
	}

	@Override
	public CompletableFuture<Void> addMembership(String id, MembershipDTO membershipDTO) {
		LOGGER.info("Adding member group with id: {} membership {}", id, membershipDTO);

		Membership membership= convertFromDTO( membershipDTO);
		Query query = new Query(Criteria.where("id").is(id));

		Update update = new Update().push("memberships", membership);

		WriteResult result= mongoTemplate.updateFirst(query, update, RGroup.class);
		LOGGER.info( "Add member "+ result);

		return CompletableFuture.completedFuture(null);
	}

	
	@Override
	public CompletableFuture<Void> deleteMembership(String id, String memid) {
		LOGGER.info("Deleting member group with id: {} membership {}", id, memid);


		WriteResult result= mongoTemplate.updateMulti(
				new Query(Criteria.where("id").is(id)),
				new Update().pull("memberships", Query.query(Criteria.where("person.id").is(memid))),RGroup.class);
		LOGGER.info( "Delete member "+ result);

		return CompletableFuture.completedFuture(null);
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

	private CompletableFuture<RGroup> findGroupById(String id) {
		return repository.findOne(id);
	}
}
