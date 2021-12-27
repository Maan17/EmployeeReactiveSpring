package com.employee.employee.employeeController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.employee.employee.EmployeeDao.EmployeeDao;
import com.employee.employee.EmployeeModel.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/employees")
public class employeeController {

	private EmployeeDao employeeDAO;

	public employeeController(EmployeeDao employeeDAO){this.employeeDAO = employeeDAO;}

	@GetMapping
	public Flux<Employee> getAllEmployees(){ return employeeDAO.findAll();}

	@GetMapping("{id}")
	public Mono<ResponseEntity<Employee>> getEmployee(@PathVariable String id){
		return employeeDAO.findById(id)
				.map(employee -> ResponseEntity.ok(employee))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Employee> saveEmployee(@RequestBody Employee employee){ return employeeDAO.save(employee);}

	@PutMapping("{id}")
	public Mono<ResponseEntity<Employee>> updateEmployee(@PathVariable(value = "id") String id,
														 @RequestBody Employee employee){
		return employeeDAO.findById(id)
				.flatMap(existingEmployee ->{
					existingEmployee.setEmpName(employee.getEmpName());
					existingEmployee.setDepartment(employee.getDepartment());
					existingEmployee.setSalary(employee.getSalary());
					existingEmployee.setPosition(employee.getPosition());
					return employeeDAO.save(existingEmployee);
				})
				.map(updateEmployee -> ResponseEntity.ok(updateEmployee))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("{id}")
	public Mono<ResponseEntity<Void>> deleteEmployee(@PathVariable(value = "id") String id){
		return employeeDAO.findById(id)
				.flatMap(existingEmployee ->
						employeeDAO.delete(existingEmployee)
								.then(Mono.just(ResponseEntity.ok().<Void>build()))
				)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping
	public Mono<Void> deleteAllEmployees(){
		return employeeDAO.deleteAll();
	}


//	@RequestMapping(value = "/employee/maxSalary", method = RequestMethod.GET)
//	@ResponseBody
//	public Map< String, Employee> maxSalary(){
//		List<Employee> employees = employeeDAO.findAll();
//
//		Map< String, Employee> maxSalaryEmp = employees.stream()
//				.collect(Collectors.toMap(Employee::getDepartment, Function.identity(), BinaryOperator.maxBy(Comparator.comparing(Employee::getSalary))));
//
//		return maxSalaryEmp;
//	}
}