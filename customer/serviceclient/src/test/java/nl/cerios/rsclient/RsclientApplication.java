package nl.cerios.rsclient;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import nl.remco.group.service.dto.PersonDTO;
import nl.remco.scope.service.ScopeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.lang.System.out;

@SpringBootApplication
public class RsclientApplication {
  private WebClient webClient;
  private static final Logger logger = LoggerFactory.getLogger(RsclientApplication.class);

  ScopeClient scopeClient = new ScopeClient();

  @Bean
  WebClient client() {
    return WebClient.create("http://localhost:8082");
  }


  @Bean
  CommandLineRunner rsclient(WebClient client) {
    return args -> {
      this.webClient = client;
      if (false) {
        tester().switchIfEmpty(Mono.just(new GroupDTO())).subscribe(o -> System.exit(0), o -> {
          o.printStackTrace();
          System.exit(1);
        });
      } else {
        wrongService().flatMap(this::print).subscribe((ClientResponse o1) -> {
          out.println("Output:" + o1.statusCode());
          System.exit(0);
        }, o -> {
          o.printStackTrace();
          System.exit(1);
        });
      }
    };
  }

  private Mono<GroupDTO> tester() {
    return getGroups()
      .flatMap(groups1 ->
        scopeClient.insertScope()
          .flatMap(insertedScope ->
            insertGroup(insertedScope)
              .flatMap(insertedGroup -> getGroups()
                .flatMap(groups2 ->
                  getGroup(insertedGroup)
                    .flatMap(group -> test1(groups1, groups2, group, insertedScope))))));
  }

  private Mono<GroupDTO> getGroup(GroupDTO group) {
    return webClient.get().uri("/api/group/" + group.getId()).exchange()
      .flatMap(clientResponse -> clientResponse.bodyToMono(GroupDTO.class));

  }

  private Mono<GroupDTO> test1(List<GroupDTO> groups1, List<GroupDTO> groups2, GroupDTO group, ScopeDTO insertedScope) {
    logger.info("" + group + " " + insertedScope);
    logger.info("List size" + groups1.size() + " " + groups2.size());

    assert (group.getScope().getName().equals("Cyber"));
    assert (groups1.size() + 1 == groups2.size());
    return addMember(group).then(getGroup(group)).flatMap(this::test2);
  }

  private Mono<GroupDTO> test2(GroupDTO group) {
    logger.info("" + group);
    assert (group.getMemberships().get(0).getRole().equals("leider"));
    assert (group.getMemberships().get(0).getPerson().getName().equals("Bob"));
    return Mono.just(group);
  }

  private Mono<Object> print(final Object message) {
    return Mono.fromCompletionStage(CompletableFuture.runAsync(() -> {
      logger.info("" + message);
    }));
  }

  private Mono<List<GroupDTO>> getGroups() {
    return webClient.get().uri("/api/group").exchange().flux()
      .flatMap(clientResponse -> clientResponse.bodyToFlux(GroupDTO.class))
      .collectList();
  }

  private Mono<GroupDTO> insertGroup(ScopeDTO scopeDTO) {
    GroupDTO group = new GroupDTO();
    group.setName("Next Generation");
    group.setDescription("This is the beginning of an amazing story");

    nl.remco.group.service.dto.ScopeDTO scope = new nl.remco.group.service.dto.ScopeDTO();
    scope.setId(scopeDTO.getId());
    group.setScope(scope);

    Mono<GroupDTO> savedGroup = webClient.post().uri("/api/group").syncBody(group).exchange()
      .flatMap(clientResponse -> clientResponse.bodyToMono(GroupDTO.class));
    return savedGroup;
  }

  private Mono<ClientResponse> addMember(GroupDTO group) {
    MembershipDTO mmbr = new MembershipDTO();
    mmbr.setRole("leider");
    PersonDTO person = new PersonDTO("person1");
    mmbr.setPerson(person);
    Mono<ClientResponse> chain = webClient.post().uri("/api/group/" + group.getId() + "/membership").syncBody(mmbr).exchange();
    return chain;
  }


  public void testWrongService() {
//    /flatMap(this::print).
    wrongService().subscribe(clientResponse -> {
      logger.info("Status " + clientResponse.statusCode());
    }, o -> {
      o.printStackTrace();
    });
  }

  private Mono<ClientResponse> wrongService() {

    Mono<ClientResponse> chain = client().get().uri("/nonexisting/").exchange().flatMap(this::check);
    return chain;
  }

  private Mono<ClientResponse> print(final ClientResponse clientResponse) {
    return Mono.fromCompletionStage(CompletableFuture.supplyAsync(() -> {
      logger.info("Status " + clientResponse.statusCode());
      return clientResponse;
    }));
  }

  private Mono<ClientResponse> check(final ClientResponse clientResponse) {
    if (clientResponse.statusCode().value() >= 200 && clientResponse.statusCode().value() < 300)
      return Mono.just(clientResponse);
    else
      return clientResponse.bodyToMono(String.class).flatMap(message -> Mono.error(new Exception(message)));
  }


  public static void main(String[] args) {
    SpringApplication.run(RsclientApplication.class, args);
  }
}

