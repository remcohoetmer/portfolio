package nl.cerios.rsclient;

import lombok.*;

import java.util.Date;
import java.util.UUID;


public class TestLombok {

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