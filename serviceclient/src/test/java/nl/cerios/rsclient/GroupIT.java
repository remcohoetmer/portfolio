package nl.cerios.rsclient;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.scope.service.ScopeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class GroupIT {

  private static final Logger logger = LoggerFactory.getLogger(GroupIT.class);

  @Autowired
  ScopeClient scopeClient;

  @Autowired
  GroupClient groupClient;

  @Bean
  CommandLineRunner rsclient(WebClient client) {
    return args -> {
      tester().switchIfEmpty(Mono.just(new GroupDTO())).subscribe(o -> System.exit(0), o -> {
        o.printStackTrace();
        System.exit(1);
      });
    };
  }

  private Mono<GroupDTO> tester() {
    return groupClient.getGroups()
      .flatMap(groups1 ->
        scopeClient.insertScope()
          .flatMap(insertedScope ->
            groupClient.insertGroup(insertedScope)
              .flatMap(insertedGroup -> groupClient.getGroups()
                .flatMap(groups2 ->
                  groupClient.getGroup(insertedGroup)
                    .flatMap(group -> test1(groups1, groups2, group, insertedScope))))));
  }


  private Mono<GroupDTO> test1(List<GroupDTO> groups1, List<GroupDTO> groups2, GroupDTO group, ScopeDTO insertedScope) {
    logger.info("" + group + " " + insertedScope);
    logger.info("List size" + groups1.size() + " " + groups2.size());

    assert (group.getScope().getName().equals("Cyber"));
    assert (groups1.size() + 1 == groups2.size());
    return groupClient
      .addMember(group)
      .then(groupClient.getGroup(group))
      .flatMap(this::test2);
  }

  private Mono<GroupDTO> test2(GroupDTO group) {
    logger.info("" + group);
    assert (group.getMemberships().get(0).getRole().equals("leider"));
    assert (group.getMemberships().get(0).getPerson().getName().equals("Bob"));
    return Mono.just(group);
  }


  private Mono<ClientResponse> print(final ClientResponse clientResponse) {
    return Mono.fromCompletionStage(CompletableFuture.supplyAsync(() -> {
      logger.info("Status " + clientResponse.statusCode());
      return clientResponse;
    }));
  }


  public static void main(String[] args) {
    SpringApplication.run(GroupIT.class, args);
  }
}

