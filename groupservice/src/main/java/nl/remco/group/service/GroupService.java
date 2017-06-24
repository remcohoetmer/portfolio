package nl.remco.group.service;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface GroupService {
  Mono<GroupDTO> create(GroupDTO scope);

  Mono<GroupDTO> delete(String id);

  Flux<GroupDTO> find(GroupFilter groupFilter, GroupSelection groupSelection);

  Flux<GroupDTO> tail(String name);

  Mono<GroupDTO> findById(String id);

  Mono<GroupDTO> update(GroupDTO scope);

  Mono<Void> addMembership(String id, MembershipDTO membership);

  Mono<Void> deleteMembership(String id, String memid);

  Mono<Void> initialise();
}
