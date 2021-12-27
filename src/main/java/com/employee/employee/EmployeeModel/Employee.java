package com.employee.employee.EmployeeModel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "Employee")
public class Employee {


    @Id private String empNo;
    public String empName;
    public String position;
    public String department;
    public Integer salary;

    public Employee() {

    }

    public Employee(String empNo, String empName, String position,String department, Integer salary) {
        this.empNo = empNo;
        this.empName = empName;
        this.position = position;
        this.department=department;
        this.salary=salary;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(empNo, employee.empNo) && Objects.equals(empName, employee.empName) && Objects.equals(position, employee.position) && Objects.equals(department, employee.department) && Objects.equals(salary, employee.salary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empNo, empName, position, department, salary);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empNo='" + empNo + '\'' +
                ", empName='" + empName + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                '}';
    }
}
