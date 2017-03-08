package nl.remco.group.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.remco.group.service.domain.Group;
import nl.remco.group.service.dto.GroupDTO;


@Service
public class MongoDbGroupService implements GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbGroupService.class);

	private final GroupRepository repository;

	@Autowired
	PersonEnricher enricher;

	@Autowired
	ScopeEnricher scopeEnricher;

	@Autowired
	Converter converter;

	@Autowired
	MongoDbGroupService(GroupRepository repository) {
		this.repository = repository;
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
				.thenCompose( enricher::enrichPersons)
				.thenCompose( scopeEnricher::enrichScopes);
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
							}

									);
				});
	}

	@Override
	public CompletableFuture<List<GroupDTO>> findAll() {
		LOGGER.info("Finding all group entries.");

		CompletableFuture<List<Group>> groupEntries = repository.findAll();

		return groupEntries
				.thenApply(this::convertToDTOs)
				.thenCompose( enricher::enrichPersons)
				.thenCompose( scopeEnricher::enrichScopes);
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
				.thenCompose( enricher::enrichPersons);
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
				.thenCompose( enricher::enrichPersons);
	}



	private CompletableFuture<Group> findGroupById(String id) {
		return repository.findOne(id);
	}
}
