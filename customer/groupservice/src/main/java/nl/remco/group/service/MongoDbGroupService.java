package nl.remco.group.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.remco.group.domain.Group;
import nl.remco.group.domain.Membership;
import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import nl.remco.group.service.dto.PersonDTO;


@Service
public class MongoDbGroupService implements GroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbGroupService.class);

    private final GroupRepository repository;

    @Autowired
    GroupEnricher enricher;
    
    @Autowired
    MongoDbGroupService(GroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public GroupDTO create(GroupDTO group) {
        LOGGER.info("Creating a new group entry with information: {}", group);

        Group persisted = Group.getBuilder()
                .status(group.getStatus())
                .name(group.getName())
                .build();

        persisted = repository.save(persisted);
        LOGGER.info("Created a new group entry with information: {}", persisted);

        return convertToDTO(persisted);
    }

    @Override
    public GroupDTO delete(String id) {
        LOGGER.info("Deleting a group entry with id: {}", id);

        Group deleted = findGroupById(id);
        repository.delete(deleted);

        LOGGER.info("Deleted group entry with informtation: {}", deleted);

        return convertToDTO(deleted);
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
                .map(this::convertToDTO)
                .collect(toList());
    }

    @Override
    public GroupDTO findById(String id) {
        LOGGER.info("Finding group entry with id: {}", id);

        Group found = findGroupById(id);

        LOGGER.info("Found group entry: {}", found);

        return convertToDTO(found);
    }

    @Override
    public GroupDTO update(GroupDTO group) {
        LOGGER.info("Updating group entry with information: {}", group);

        Group updated = findGroupById(group.getId());
        updated.update(group.getStatus(), group.getName());
        updated = repository.save(updated);

        LOGGER.info("Updated group entry with information: {}", updated);

        return convertToDTO(updated);
    }

    private Group findGroupById(String id) {
        Optional<Group> result = repository.findOne(id);
        return result.orElseThrow(() -> new GroupNotFoundException(id));

    }

    private GroupDTO convertToDTO(Group model) {
        GroupDTO dto = new GroupDTO();

        dto.setId(model.getId());
        dto.setStatus(model.getStatus());
        dto.setName(model.getName());
        List<MembershipDTO> mbsDTO= new ArrayList<>();
        for (Membership ms: model.getMemberships()) {
        	MembershipDTO mbDTO= new MembershipDTO();
        	mbDTO.setPersoon( new PersonDTO( ms.getPersoon().getId()));
        	mbDTO.setRol( ms.getRol());
        	mbsDTO.add(mbDTO);
        }
        dto.setMemberships( mbsDTO);
        return dto;
    }
}
