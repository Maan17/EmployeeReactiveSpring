package com.employee.employee.EmployeeDao;

import com.employee.employee.EmployeeModel.Employee;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.employee.employee.EmployeeModel.Employee;
@Repository
public interface EmployeeDao extends ReactiveMongoRepository<Employee, String> {


}
