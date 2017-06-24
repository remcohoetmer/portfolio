package nl.cerios.rsclient;

import nl.remco.group.service.dto.GroupDTO;
import org.junit.Test;
import reactor.core.publisher.Mono;

import static java.lang.System.out;

public class ReactorExperience {
  @Test
  public void testMonoDieNietWordtUitgevoerdVanwegeOntbrekendeSubscription() {
    Mono<Integer> i = Mono.just(10);
    Mono<Object> d = i.map(this::convert);
    d.subscribe(out::println);
  }

  @Test
  public void testMonoDieNietWordtUitgevoerdVanwegeOntbrekendeSubscriptionOplossing() {
    Mono<Integer> i = Mono.just(10);
    Mono<Object> d = i.flatMap(this::convert);
    d.subscribe(out::println);
    //oplossing: sterke typering had het issue voorkomen: Mono<Double>
  }

  private Mono<Double> convert(Integer i) {
    return Mono.just(i.doubleValue());
  }

  @Test
  public void testMonoDieGeenAntwoordGeeft() {
    Mono<Integer> i = getData(13);
    Mono<Object> d = i.map(this::convert);
    Mono<String> result = Mono.just("OK");

    Mono<String> returnvalue = d.flatMap(ignoredOutcome -> result);
    returnvalue.subscribe(out::println);//levert niets op
  }

  @Test
  public void testMonoDieGeenAntwoordGeeftOplossing() {
    Mono<Integer> i = getData(13);
    Mono<Object> d = i.map(this::convert);
    Mono<String> result = Mono.just("OK");

    Mono<String> returnvalue = d.then(result);//.onErrorResume(e -> result);
    returnvalue.subscribe(out::println);//levert wel resultaat op
  }

  @Test
  public void testJava8TypeInferenceHulpNodig() {
    Mono<GroupDTO> gm0 = getMono().then(getGroup()); // nu kan Java het type zelf casten
    Mono<GroupDTO> gm = ((Mono<Object>) getMono()).then(getGroup()).flatMap(this::test2); //nu niet meer, hulp is nodig
  }

  public Mono getMono() {
    return Mono.just(new Object());
  }

  private Mono<GroupDTO> test2(GroupDTO group) {
    return Mono.just(group);
  }

  private Mono<Integer> getData(int i) {
    if (i == 13) {
      return Mono.empty();
    }
    return Mono.just(i);
  }


  public Mono<GroupDTO> getGroup() {
    return Mono.just(new GroupDTO());
  }

}
