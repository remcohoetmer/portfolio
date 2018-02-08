package nl.cerios.rsclient;

import nl.remco.group.service.dto.GroupDTO;
import nl.remco.group.service.dto.MembershipDTO;
import nl.remco.group.service.dto.PersonDTO;
import nl.remco.scope.service.dto.ScopeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component

public class GroupClient {
  private static final Logger logger = LoggerFactory.getLogger(GroupClient.class);

  @Bean
  WebClient client() {
    return WebClient.create("http://localhost:80");
  }

  private Mono<GroupDTO> print(final GroupDTO message) {
    return Mono.fromCompletionStage(CompletableFuture.supplyAsync(() -> {
      logger.info("" + message);
      return message;
    }));
  }

  Mono<GroupDTO> getGroup(GroupDTO group) {
    String url = "/api/group/" + group.getId();
    return client().get().uri(url).exchange()
      .flatMap(response -> check(url, response))
      .flatMap(clientResponse -> clientResponse.bodyToMono(GroupDTO.class))
      .flatMap(this::print);

  }

  Mono<List<GroupDTO>> getGroups() {
    String url = "/api/group/";
    return client().get().uri(url).exchange().flux()
      .flatMap(response -> check(url, response))
      .flatMap(clientResponse -> clientResponse.bodyToFlux(GroupDTO.class))
      .collectList();
  }

  Mono<GroupDTO> insertGroup(ScopeDTO scopeDTO) {
    GroupDTO group = new GroupDTO();
    group.setName("Next Generation");
    group.setDescription("This is the beginning of an amazing story");

    nl.remco.group.service.dto.ScopeDTO scope = new nl.remco.group.service.dto.ScopeDTO();
    scope.setId(scopeDTO.getId());
    group.setScope(scope);

    String uri = "/api/group";
    Mono<GroupDTO> savedGroup = client().post().uri(uri).syncBody(group).exchange()
      .flatMap(response -> check(uri, response))
      .flatMap(clientResponse -> clientResponse.bodyToMono(GroupDTO.class));
    return savedGroup;
  }

  Mono<ClientResponse> addMember(GroupDTO group) {
    MembershipDTO mmbr = new MembershipDTO();
    mmbr.setRole("leider");
    PersonDTO person = new PersonDTO("person1");
    mmbr.setPerson(person);
    String uri = "/api/group/" + group.getId() + "/membership";
    Mono<ClientResponse> chain = client().post().uri(uri)
      .syncBody(mmbr).exchange()
      .flatMap(response -> check(uri, response));
    return chain;
  }

  static Mono<ClientResponse> check(final String url, final ClientResponse clientResponse) {
    int status = clientResponse.statusCode().value();

    if (status >= 200 && status < 300)
      return Mono.just(clientResponse);
    else {
      String headers = clientResponse.headers().asHttpHeaders().entrySet().stream()
        .map(t -> "[" + t.getKey() + ":" + t.getValue() + "]").collect(Collectors.joining(","));
      return clientResponse.bodyToMono(String.class)
        .flatMap(message -> Mono.error(new Exception(String.format("HTTP status %d for %s: %s\nheaders: %s", status, url, message, headers))));
    }
  }
}

