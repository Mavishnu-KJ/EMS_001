package com.example.EMS_001.repository;

import com.example.EMS_001.model.dto.EmployeeRequestDto;
import com.example.EMS_001.model.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void testAddEmployee_SaveSuccessWithGeneratedId(){

        //Prepare input
        Employee employee = new Employee();
        employee.setName("Sachin");
        employee.setSalary(888888);
        employee.setDepartment("Cricket");
        employee.setEmail("sachin@gmail.com");

        //Perform
        Employee savedEmployee = testEntityManager.persistFlushFind(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(savedEmployee.getName()).isEqualTo("Sachin");

    }

    @Test
    public void testExistsByEmail_False(){

        //Prepare input
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(
                "Sachin",
                888888,
                "Cricket",
                "sachin@gmail.com"
        );

        //Perform
        boolean exists = employeeRepository.existsByEmail(employeeRequestDto.getEmail());

        assertThat(exists).isFalse();
    }

    @Test
    public void testExistsByEmail_True(){
        //Prepare input
        Employee employee = new Employee();
        employee.setName("Sachin");
        employee.setSalary(888888);
        employee.setDepartment("Cricket");
        employee.setEmail("sachin@gmail.com");

        //Save before checking existsByEmail
        testEntityManager.persistAndFlush(employee);

        //Perform
        boolean exists = employeeRepository.existsByEmail(employee.getEmail());

        assertThat(exists).isTrue();

    }


}
