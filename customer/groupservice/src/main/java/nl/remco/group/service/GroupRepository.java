package nl.remco.group.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.repository.Repository;

import nl.remco.group.service.domain.Group;


interface GroupRepository extends Repository<Group, String> {

	CompletableFuture<Void> delete(Group deleted);

    CompletableFuture<List<Group>> findAll();

    CompletableFuture<Group> findOne(String id);

    CompletableFuture<Group> save(Group saved);
}
