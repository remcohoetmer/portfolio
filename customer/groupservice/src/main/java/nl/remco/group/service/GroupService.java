package nl.remco.group.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;


public interface GroupService {
	CompletableFuture<GroupDTO> create(GroupDTO scope);
	CompletableFuture<GroupDTO> delete(String id);
    CompletableFuture<List<GroupDTO>> find(GroupFilter groupFilter, GroupSelection groupSelection);
    CompletableFuture<GroupDTO> findById(String id);
    CompletableFuture<GroupDTO> update(GroupDTO scope);
	CompletableFuture<GroupDTO> addMembership(String id, MembershipDTO membership);
}
