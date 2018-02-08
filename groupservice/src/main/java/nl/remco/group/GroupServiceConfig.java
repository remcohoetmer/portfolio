package nl.remco.group;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@PropertySource("application.properties")
public class GroupServiceConfig {

  public static void main(String[] args) {
    SpringApplication.run(new Class[]{GroupServiceConfig.class}, args);
  }
}
