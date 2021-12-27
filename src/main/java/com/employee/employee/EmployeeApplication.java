package com.employee.employee;

import com.employee.employee.EmployeeDao.EmployeeDao;
import com.employee.employee.EmployeeModel.Employee;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class EmployeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeApplication.class, args);
		System.out.println("Code Runs Here");
	}

	@Bean
	CommandLineRunner init(ReactiveMongoOperations operations, EmployeeDao employeeDao){
		return args -> {
			Flux<Employee> employeeFlux = Flux.just(
							new Employee("1", "Hannah", "Module Lead", "Payments", 10000),
							new Employee(null, "Justin", "Software Engineer", "Daily Banking", 50000),
							new Employee(null, "Clay", "Analyst", "SBCP", 20000))
					.flatMap(employeeDao::save);

			employeeFlux
					.thenMany(employeeDao.findAll())
					.subscribe(System.out::println);

			operations.collectionExists(Employee.class)
					.flatMap(exists -> exists ? operations.dropCollection(Employee.class) : Mono.just(exists))
					.thenMany(v -> operations.createCollection(Employee.class))
					.thenMany(employeeFlux)
					.thenMany(employeeDao.findAll())
					.subscribe(System.out::println);
		};
	}
}
