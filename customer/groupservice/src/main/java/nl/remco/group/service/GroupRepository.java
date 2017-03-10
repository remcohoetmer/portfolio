package nl.remco.group.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.data.repository.Repository;
import nl.remco.group.service.domain.RGroup;

interface GroupRepository extends Repository<RGroup, String> {
	CompletableFuture<Void> delete(RGroup deleted);
    CompletableFuture<List<RGroup>> findAll();
    CompletableFuture<RGroup> findOne(String id);
    CompletableFuture<RGroup> save(RGroup saved);
}
