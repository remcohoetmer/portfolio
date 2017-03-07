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
	GroupEnricher enricher;

	@Autowired
	Converter converter;

	@Autowired
	MongoDbGroupService(GroupRepository repository) {
		this.repository = repository;
	}

	@Override
	public GroupDTO create(GroupDTO group) {
		LOGGER.info("Creating a new group entry with information: {}", group);

		Group persisted = converter.convertfromDTO(group);
		
		persisted = repository.save(persisted);
		LOGGER.info("Created a new group entry with information: {}", persisted);

		return converter.convertToDTO(persisted);
	}

	@Override
	public GroupDTO delete(String id) {
		LOGGER.info("Deleting a group entry with id: {}", id);

		Group deleted = findGroupById(id);
		repository.delete(deleted);

		LOGGER.info("Deleted group entry with informtation: {}", deleted);

		return converter.convertToDTO(deleted);
	}

	@Override
	public CompletableFuture<List<GroupDTO>> findAll() {
		LOGGER.info("Finding all group entries.");

		CompletableFuture<List<Group>> groupEntries = repository.findAll();

		return groupEntries
				.thenApply(entries -> {
					LOGGER.info("Found {} group entries", entries.size());
					return convertToDTOs(entries);
				})
				.thenCompose( enricher::enrichPersons);
	}

	private List<GroupDTO> convertToDTOs(List<Group> models) {
		return models.stream()
				.map(converter::convertToDTO)
				.collect(toList());
	}

	@Override
	public GroupDTO findById(String id) {
		LOGGER.info("Finding group entry with id: {}", id);

		Group found = findGroupById(id);

		LOGGER.info("Found group entry: {}", found);

		return converter.convertToDTO(found);
	}

	@Override
	public GroupDTO update(GroupDTO group) {
		LOGGER.info("Updating group entry with information: {}", group);

		Group updated = findGroupById(group.getId());
		updated.update(group.getStatus(), group.getName());
		updated = repository.save(updated);

		LOGGER.info("Updated group entry with information: {}", updated);

		return converter.convertToDTO(updated);
	}

	private Group findGroupById(String id) {
		Optional<Group> result = repository.findOne(id);
		return result.orElseThrow(() -> new GroupNotFoundException(id));

	}


}
