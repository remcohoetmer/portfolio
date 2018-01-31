package nl.remco.group.service;

import nl.remco.group.enrich.PersonEnricher;
import nl.remco.group.enrich.ScopeEnricher;
import nl.remco.group.kafka.SampleProducer;
import nl.remco.group.organisation.service.OrganisationEnricher;
import nl.remco.group.service.domain.*;
import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import nl.remco.group.service.serviceclients.ScopeClient;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

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

  @Autowired
  SampleProducer kafkaSender;
  private static final String TOPIC = "demo-topic";

  private Mono<GroupDTO> enrich(GroupDTO group, GroupFilter groupFilter,
                                GroupSelection groupSelection) {
    Mono<GroupDTO> chain = Mono.just(group);
    if (groupSelection.isSelectPersons())
      chain = chain.flatMap(personEnricher::enrichPersons);
    if (groupSelection.isSelectOrganisations())
      chain = chain.flatMap(organisationEnricher::enrichOrganisations);
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
  public Mono<GroupDTO> create(GroupDTO groupDTO) {
    logger.info("Creating a new group entry with information: {}", groupDTO);
    Group group = converter.convertfromDTO(groupDTO);
    return repository.save(group)
      .map(converter::convertToDTO)
      .log()
      /*
      .flatMap(dto -> {
        try {
          return kafkaSender.sendMessages(TOPIC, group).then(Mono.just(dto));
        } catch (InterruptedException e) {
          throw Exceptions.propagate(e);
        }
      })*/;
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

  @Override
  public Flux<GroupDTO> tail(final String name) {
    // todo: blokkeert bij 2 gelijktijdige requests
    //return this.mongoTemplate.tail(Query.query(Criteria.where("name").is(name)), Group.class).concatMap(converter::convertToDTO_RS);
    return this.mongoTemplate.tail(Query.query(Criteria.where("name").is(name)), Group.class).map(converter::convertToDTO);
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
      .map(found -> {// Found cannot be null!
        logger.info("Found group entry: {}", found);
        return converter.convertToDTO(found);

      })
      .flatMap(personEnricher::enrichPersons)
      .flatMap(scopeEnricher::enrichScopes)
      .flatMap(organisationEnricher::enrichOrganisations)
      .switchIfEmpty(Mono.error(new IllegalArgumentException(("Group does not exists"))));
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
      }).then();
  }


  @Override
  public Mono<Void> deleteMembership(String id, String memid) {
    logger.info("Deleting member group with id: {} membership {}", id, memid);


    return mongoTemplate.updateMulti(
      new Query(Criteria.where("id").is(id)),
      new Update().pull("memberships", Query.query(Criteria.where("person.id").is(memid))), Group.class)
      .log("Delete member result" + id + "/ " + memid, Level.INFO)
      .then();
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

  private Mono<List<Group>> getInitalGroups() {
    Flux<Scope> scopes = new ScopeClient().getScopes();
    return scopes.collectList().map(
      list -> {
        Scope scope0 = (list != null && list.size() > 0) ? list.get(0) : null;
        Scope scope1 = (list != null && list.size() > 1) ? list.get(1) : null;
        Scope scope2 = (list != null && list.size() > 2) ? list.get(2) : null;
        Scope scope3 = (list != null && list.size() > 3) ? list.get(3) : null;

        List<Membership> members = new ArrayList();
        members.add(new Membership("leerling", new Person("person1")));
        members.add(new Membership("leerling", new Person("person2")));
        members.add(new Membership("leerling", new Person("person3")));
        members.add(new Membership("docent", new Person("person4")));

        return Arrays.asList(
          Group.getBuilder().name("filosofie").withDescription("Descartes").status("Active").withOrganisation(new Organisation("8000")).withScope(scope0).withMembers(members).build(),
          Group.getBuilder().name("geo").withDescription("Leonardo da Vinci").status("Active").withOrganisation(new Organisation("8000")).withScope(scope1).build(),
          Group.getBuilder().name("natuurkunde").withDescription("Gay-Lussac").status("Active").withOrganisation(new Organisation("8001")).withScope(scope2).build(),
          Group.getBuilder().name("wiskunde").withDescription("Pythogoras").status("Active").withOrganisation(new Organisation("8002")).withScope(scope3).build()
        );
      });
  }

  @Override
  public Mono<Void> initialise() {
    final Mono<Void> initializeCollections =
      mongoTemplate.dropCollection(Group.class)
        .then(mongoTemplate.createCollection(Group.class))
//      .then(mongoTemplate.createCollection(Group.class, CollectionOptions.empty().capped(104857600))) // max: 100MBytes
        .then();

    final Mono<Void> initializeData =
      mongoTemplate
        .insertAll(getInitalGroups(), Group.class)
        .log(Group.class.getName(), Level.INFO)
        .then();

    return initializeCollections.then(initializeData);
  }

  private Mono<Group> findGroupById(String id) {
    return repository.findById(id);
  }
}
