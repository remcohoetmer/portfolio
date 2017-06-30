package nl.remco.scope.service;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


interface ScopeRepository extends ReactiveMongoRepository<Scope, String> {
}
