package nl.cerios.rsclient;

import lombok.*;
import nl.cerios.rsclient.dto.GroupDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication

public class RsclientApplication {
  @Bean
  WebClient client() {
    return WebClient.create("http://localhost:8082");
  }

  @Bean
  CommandLineRunner rsclient(WebClient client) {
    return args -> {
      client.get().uri("/api/group").exchange().flux()
        .flatMap(clientResponse -> clientResponse.bodyToFlux(GroupDTO.class))
        //   .filter(m -> m.getTitle().contains("Jedi"))
        .subscribe(movie ->
          {
            System.out.println( movie);
/*
            client
              .get()
              .uri("/movies/{id}/events", movie.getId().toString())
              .exchange()
              .flux()
              .flatMap(me -> me.bodyToFlux(MovieEvent.class))
              .subscribe(System.out::println);
              */
          }
        );
    };
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