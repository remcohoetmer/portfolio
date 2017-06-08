package nl.remco.group.service;

import com.mongodb.client.result.UpdateResult;
import nl.remco.group.enrich.PersonEnricher;
import nl.remco.group.enrich.ScopeEnricher;
import nl.remco.group.organisation.service.OrganisationEnricher;
import nl.remco.group.service.domain.Membership;
import nl.remco.group.service.domain.Person;
import nl.remco.group.service.domain.Group;
import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MongoDBGroupService implements GroupService {

  private static final Logger logger = LoggerFactory.getLogger(MongoDBGroupService.class);
  private final GroupRepository repository;

  @Autowired
  private ReactiveMongoTemplate mongoTemplate;

  @Autowired
  private OrganisationEnricher organisationEnricher;

  @Autowired
  private PersonEnricher personEnricher;

  @Autowired
  private ScopeEnricher scopeEnricher;

  @Autowired
  private Converter converter;

  @Autowired
  private MongoDBGroupService(GroupRepository repository) {
    this.repository = repository;
  }

  private Mono<GroupDTO> enrich(GroupDTO group, GroupFilter groupFilter,
                                GroupSelection groupSelection) {
    Mono<GroupDTO> chain = Mono.just(group);
    if (groupSelection.isSelectOrganisations())
      chain = chain.flatMap(organisationEnricher::enrichOrganisations);

    if (groupSelection.isSelectPersons())
      chain = chain.flatMap(personEnricher::enrichPersons);
    if (groupSelection.isSelectScopes())
      chain = chain.flatMap(scopeEnricher::enrichScopes);
    if (groupSelection.isSelectMasters())
      chain = chain.flatMap(group1 -> enrichMaster(group1, groupFilter));
    return chain;
  }

  private Mono<GroupDTO> enrichMaster(GroupDTO groupDTO, GroupFilter groupFilter) {
    if (groupDTO.getMaster() != null
      && groupDTO.getMaster().getId() != null
      && !groupDTO.getMaster().getId().isEmpty()) {

      return findById(groupDTO.getMaster().getId())
        .map(master -> {
          groupDTO.setMaster(master);
          return groupDTO;
        });

    }
    return Mono.just(groupDTO);
  }

  @Override
  public Mono<GroupDTO> create(GroupDTO group) {
    logger.info("Creating a new group entry with information: {}", group);
    Group groupDTO = converter.convertfromDTO(group);
    return repository.save(groupDTO)
      .map(converter::convertToDTO);
  }

  @Override
  public Mono<GroupDTO> delete(String id) {
    logger.info("Deleting a group entry with id: {}", id);

    return findGroupById(id)
      .flatMap(group -> {
        return repository.deleteById(id)
          .map(dummy -> {
            logger.info("Deleted group entry with information: {}" + group);
            return converter.convertToDTO(group);
          });
      });
  }

  @Override
  public Flux<GroupDTO> find(GroupFilter groupFilter, GroupSelection groupSelection) {
    logger.info("Finding all group entries.");

    Flux<GroupDTO> groupEntries = findGroups(groupFilter)
      .map(converter::convertToDTO)
      .flatMap(group -> enrich(group, groupFilter, groupSelection));
    return groupEntries;
  }

  private Flux<Group> findGroups(GroupFilter groupFilter) {
    Query query = new Query();
    if (groupFilter.getName() != null) {
      query.addCriteria(Criteria.where("name").regex(".*" + groupFilter.getName() + ".*"));
    }
    if (groupFilter.getDescription() != null) {
      query.addCriteria(Criteria.where("description").regex(groupFilter.getDescription()));
    }
    if (groupFilter.getStatus() != null) {
      query.addCriteria(Criteria.where("status").is(groupFilter.getStatus()));
    }
    if (groupFilter.getCode() != null) {
      query.addCriteria(Criteria.where("code").is(groupFilter.getCode()));
    }
    if (groupFilter.getPersonId() != null) {
      query.addCriteria(Criteria.where("memberships.person.id").is(groupFilter.getPersonId()));
    }
    if (groupFilter.getMasterId() != null) {
      query.addCriteria(Criteria.where("master.id").is(groupFilter.getMasterId()));
    }
    if (groupFilter.getScopeId() != null) {
      query.addCriteria(Criteria.where("scope.id").is(new ObjectId(groupFilter.getScopeId())));
    }
    if (groupFilter.getOrganisationId() != null) {
      query.addCriteria(Criteria.where("organisation.id").is(groupFilter.getOrganisationId()));
    }
    if (groupFilter.getFeatures() != null && !groupFilter.getFeatures().isEmpty()) {
      query.addCriteria(Criteria.where("features").in(groupFilter.getFeatures()));
    }

    logger.info("Query" + query.getQueryObject().toJson());
    return mongoTemplate.find(query, Group.class);

  }

  @Override
  public Mono<GroupDTO> findById(String id) {
    logger.info("Finding group entry with id: {}", id);

    return findGroupById(id)
      .map(found -> {// Found can be null!
        logger.info("Found group entry: {}", found);
        return converter.convertToDTO(found);

      })
      .flatMap(personEnricher::enrichPersons)
      .flatMap(scopeEnricher::enrichScopes)
      .flatMap(organisationEnricher::enrichOrganisations)
      .switchIfEmpty(Mono.just(new GroupDTO()));
  }

  private Membership convertFromDTO(MembershipDTO membershipDTO) {
    Membership membership = new Membership();
    membership.setPerson(new Person(membershipDTO.getPerson().getId(), membershipDTO.getPerson().getName()));
    membership.setRole(membershipDTO.getRole());
    return membership;
  }

  @Override
  public Mono<Void> addMembership(String id, MembershipDTO membershipDTO) {
    logger.info("Adding member group with id: {} membership {}", id, membershipDTO);

    Membership membership = convertFromDTO(membershipDTO);
    Query query = new Query(Criteria.where("id").is(id));

    Update update = new Update().push("memberships", membership);

    return mongoTemplate.updateFirst(query, update, Group.class)
      .map(result -> {
        logger.info("Add member " + result);
        return new Object();
      }).then(Mono.empty());
  }


  @Override
  public Mono<Void> deleteMembership(String id, String memid) {
    logger.info("Deleting member group with id: {} membership {}", id, memid);


    Mono<UpdateResult> result = mongoTemplate.updateMulti(
      new Query(Criteria.where("id").is(id)),
      new Update().pull("memberships", Query.query(Criteria.where("person.id").is(memid))), Group.class);
    logger.info("Delete member " + result);

    return Mono.empty();
  }

  @Override
  public Mono<GroupDTO> update(GroupDTO group) {
    logger.info("Updating group entry with information: {}", group);

    return findGroupById(group.getId())
      .map(updated -> {
        updated.update(group.getStatus(), group.getName());
        return updated;
      })
      .flatMap(repository::save)
      .map(updated -> {
        logger.info("Updated group entry with information: {}", updated);

        return converter.convertToDTO(updated);
      });
  }

  private Mono<Group> findGroupById(String id) {
    return repository.findById(id);
  }
}
