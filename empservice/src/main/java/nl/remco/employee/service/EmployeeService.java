package nl.remco.employee.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


interface EmployeeService {
  Mono<EmployeeDTO> create(EmployeeDTO scope);

  Mono<EmployeeDTO> delete(String id);

  Flux<EmployeeDTO> findAll(String status);

  Mono<EmployeeDTO> findById(String id);

  Mono<EmployeeDTO> update(EmployeeDTO scope);

  Mono<Void> initialise();
}
