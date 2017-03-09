package nl.remco.group.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	private CompletableFuture<List<GroupDTO>> enrich( CompletableFuture<List<GroupDTO>> groups_cf)
	{
		return groups_cf.thenCompose( groups-> {
			/*		if (selection.isSelectPersons()) {
			cf=enrichPersons( groups);
			 */
			List<CompletableFuture<?>> list= new ArrayList<>();
			list.addAll( organisationEnricher.enrichOrganisations(groups));
			list.addAll( personEnricher.enrichPersons(groups));
			list.addAll( scopeEnricher.enrichScopes(groups));
			return CompletableFuture.allOf(list.stream().toArray(CompletableFuture[]::new))
					.thenApply( dummy-> groups);
		});
	}

	@Override
	public CompletableFuture<GroupDTO> create(GroupDTO group) {
		LOGGER.info("Creating a new group entry with information: {}", group);

		Group groupDTO = converter.convertfromDTO(group);

		return repository.save(groupDTO)
				.thenApply( persisted-> {
					LOGGER.info("Created a new group entry with information: {}", persisted);

					return converter.convertToDTO(persisted);
				})
				.thenCompose( personEnricher::enrichPersons)
				.thenCompose( scopeEnricher::enrichScopes)
				.thenCompose( organisationEnricher::enrichOrganisations);
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
	public CompletableFuture<List<GroupDTO>> findAll() {
		LOGGER.info("Finding all group entries.");

		CompletableFuture<List<GroupDTO>> groupEntries =
				repository.findAll()
				.thenApply(this::convertToDTOs);
		return enrich( groupEntries);
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
				})
				.thenCompose( personEnricher::enrichPersons)
				.thenCompose( scopeEnricher::enrichScopes)
				.thenCompose( organisationEnricher::enrichOrganisations);
	}



	private CompletableFuture<Group> findGroupById(String id) {
		return repository.findOne(id);
	}
}
