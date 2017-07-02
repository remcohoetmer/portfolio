package nl.remco.employee.service;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
}
