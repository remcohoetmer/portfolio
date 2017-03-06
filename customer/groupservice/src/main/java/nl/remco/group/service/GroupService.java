package nl.remco.group.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import nl.remco.group.service.dto.GroupDTO;


public interface GroupService {
	GroupDTO create(GroupDTO scope);
    GroupDTO delete(String id);
    CompletableFuture<List<GroupDTO>> findAll();
    GroupDTO findById(String id);
    GroupDTO update(GroupDTO scope);
}
