package nl.cerios.rsclient;

import org.junit.Test;
import reactor.core.publisher.Mono;

import static java.lang.System.out;

public class ReactorTest {
  @Test
  public void testMonoDieNietWordtUitgevoerdVanwegeOntbrekendeSubscription() {
    Mono<Integer> i = Mono.just(10);
    Mono<Object> d= i.map(this::convert);// hier moet een flatMap worden gedaan
    d.subscribe(out::println);
    //oplossing: sterke typering had had issue voorkomen: Mono<Double>
  }

  private Mono<Double> convert(Integer i) {
    return Mono.just(i.doubleValue());
  }
}
