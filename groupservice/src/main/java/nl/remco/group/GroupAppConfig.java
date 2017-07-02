package nl.remco.group;

import nl.remco.employee.EmployeeConfig;
import nl.remco.scope.ScopeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@EnableAutoConfiguration
@ComponentScan
@PropertySource("application.properties")
public class GroupAppConfig {

  public static void main(String[] args) {
    SpringApplication.run(new Class[]{GroupAppConfig.class, ScopeConfig.class, EmployeeConfig.class}, args);
  }
}
