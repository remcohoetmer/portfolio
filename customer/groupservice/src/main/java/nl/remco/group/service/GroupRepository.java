package nl.remco.group.service;

import nl.remco.group.service.domain.RGroup;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface GroupRepository extends ReactiveMongoRepository<RGroup, String> {
}
