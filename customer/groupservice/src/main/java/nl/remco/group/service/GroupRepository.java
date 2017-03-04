package nl.remco.group.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.repository.Repository;

import nl.remco.group.domain.Group;


interface GroupRepository extends Repository<Group, String> {

    void delete(Group deleted);

    CompletableFuture<List<Group>> findAll();

    Optional<Group> findOne(String id);

    Group save(Group saved);
}
