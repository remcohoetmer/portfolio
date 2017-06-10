package nl.cerios.rsclient;

import lombok.*;
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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class RsclientApplication {
  private WebClient webClient;
  private static final Logger log = LoggerFactory.getLogger(RsclientApplication.class);

  @Bean
  WebClient client() {
    return WebClient.create("http://localhost:8082");
  }


  @Bean
  CommandLineRunner rsclient(WebClient client) {
    return args -> {
      this.webClient = client;

      tester().switchIfEmpty(Mono.just(new Object())).subscribe(o -> System.exit(0), o -> System.exit(1));
      //addMember(new GroupDTO("593859364ba8d00bf5f230b7")).subscribe(o -> System.exit(0), o -> System.exit(1));
    };
  }

  private Mono<Object> tester() {
    return getGroups()
      .flatMap(groups1 ->
        insertScope()
          .flatMap(insertedScope ->
            insertGroup(insertedScope)
              .flatMap(insertedGroup -> getGroups()
                .flatMap(groups2 ->
                  getGroup(insertedGroup)
                    .flatMap(group -> test1(groups1, groups2, group, insertedScope))))));
    // als je map doet, dan compileert het ook
  }

  private Mono<Object> tester2() {
    return getGroups()
      .flatMap(groups1 ->
        insertScope()
          .flatMap(insertedScope ->
            insertGroup(insertedScope)
              .flatMap(insertedGroup -> getGroups()
                .flatMap(groups2 ->
                  getGroup(insertedGroup)
                    .flatMap(group -> test1(groups1, groups2, group, insertedScope))))));
    // als je map doet, dan compileert het ook
  }

  private Mono<GroupDTO> getGroup(GroupDTO group) {
    return webClient.get().uri("/api/group/" + group.getId()).exchange()
      .flatMap(clientResponse -> clientResponse.bodyToMono(GroupDTO.class));

  }

  private Mono<Object> test1(List<GroupDTO> groups1, List<GroupDTO> groups2, GroupDTO group, ScopeDTO insertedScope) {
    log.info("" + group + " " + insertedScope);
    log.info("List size" + groups1.size() + " " + groups2.size());

    assert (group.getScope().getName().equals("Cyber"));
    assert (groups1.size() + 1 == groups2.size());
    Mono<GroupDTO> gm= addMember(group).flatMap(dummy -> getGroup(group));
    return gm.flatMap(this::test2);
  }

  private Mono<Object> test2(GroupDTO group) {
    log.info("" + group);
    assert (group.getMemberships().get(0).getRole().equals("leider"));
    assert (group.getMemberships().get(0).getPerson().getName().equals("Bob"));
    return Mono.empty();
  }

  private Mono print(String message) {
    return Mono.just(new Object()).map(dummy -> {
      log.info(message);
      return new Object();
    });
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

  private Mono addMember(GroupDTO group) {
    MembershipDTO mmbr = new MembershipDTO();
    mmbr.setRole("leider");
    PersonDTO person = new PersonDTO("person1");
    mmbr.setPerson(person);
    Mono chain = webClient.post().uri("/api/group/" + group.getId() + "/membership").syncBody(mmbr).exchange();
    return chain;
  }

  private Mono<ScopeDTO> insertScope() {
    ScopeDTO group = new ScopeDTO();
    group.setName("Cyber");
    group.setStatus("Actief");
    Mono<ScopeDTO> savedScope = webClient.post().uri("/api/scope").syncBody(group).exchange()
      .flatMap(clientResponse -> clientResponse.bodyToMono(ScopeDTO.class));
    return savedScope;
  }

  public static void main(String[] args) {
    SpringApplication.run(RsclientApplication.class, args);
  }
}

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
class MovieEvent {
  private Movie movie;

  private Date dateTime;

}

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
@Getter
class Movie {
  private UUID id;
  private String title, genre;
}