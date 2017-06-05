package nl.remco.group.service;

import nl.remco.group.service.domain.RGroup;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

interface GroupRepository extends Repository<RGroup, String> {
  CompletableFuture<Void> delete(RGroup deleted);

  CompletableFuture<List<RGroup>> findAll();

  CompletableFuture<RGroup> findById(String id);

  CompletableFuture<RGroup> save(RGroup saved);
}
