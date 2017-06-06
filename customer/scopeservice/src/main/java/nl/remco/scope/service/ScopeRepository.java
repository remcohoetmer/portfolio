package nl.remco.scope.service;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;


interface ScopeRepository extends Repository<Scope, String> {

    void delete(Scope deleted);

    List<Scope> findAll();

    Optional<Scope> findById(String id);

    Scope save(Scope saved);
}
