package com.employee.employee;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.employee.employee.EmployeeDao.EmployeeDao;
import com.employee.employee.EmployeeModel.Employee;
import com.employee.employee.employeeController.employeeController;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
@ImportAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class JunitTests {
	private WebTestClient client;

	private List<Employee> expectedList;

	 @Autowired
	    private EmployeeDao employeeDao;

	 @Autowired
	 private employeeController controller;

	@BeforeEach
	void beforeEach(){
		this.client =
				WebTestClient
						.bindToController(new employeeController(employeeDao))
						.configureClient()
						.baseUrl("/employees")
						.build();

		this.expectedList =
				employeeDao.findAll().collectList().block();
	}

	@Test
	void testGetAllEmployees(){
		client.get()
				.uri("/")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(Employee.class)
				.isEqualTo(expectedList);
	}

	@Test
	void testGetEmployee() {
		Employee expectedEmployee = expectedList.get(0);
		client.get()
				.uri("/{id}", expectedEmployee.getEmpNo())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(Employee.class)
				.isEqualTo(expectedEmployee);
	}

	@Test
	void testEmployeeInvalidNotFound(){
		client.get()
				.uri("/aaa")
				.exchange()
				.expectStatus()
				.isNotFound();
	}

	@Test
	void testUpdateEmployees(){
		Employee updateEmployee = new Employee("1", "Justin", "demo", "demo", 10000);
		client.put()
				.uri("/{id}",expectedList.get(0).getEmpNo())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(updateEmployee), Employee.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.empName").isEqualTo("Justin");
	}

	@Test
	@Rollback(value = false)
	void testDeleteEmployee() {
		client.delete()
				.uri("/{id}", expectedList.get(0).getEmpNo())
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
	
}
