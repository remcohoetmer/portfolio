package nl.remco.group.service;

import nl.remco.group.service.domain.Group;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface GroupRepository extends ReactiveMongoRepository<Group, String> {
}
