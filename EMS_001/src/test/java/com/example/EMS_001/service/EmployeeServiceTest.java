package com.example.EMS_001.service;

import com.example.EMS_001.exceptions.DuplicateEmailException;
import com.example.EMS_001.model.dto.EmployeeRequestDto;
import com.example.EMS_001.model.dto.EmployeeResponseDto;
import com.example.EMS_001.model.entity.Employee;
import com.example.EMS_001.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EmployeeServiceTest {

    @Autowired
    EmployeeService employeeService;

    @MockitoBean
    EmployeeRepository employeeRepository;

    @MockitoBean
    ModelMapper modelMapper;

    private EmployeeRequestDto employeeRequestDto;
    private EmployeeResponseDto employeeResponseDto;
    private Employee employee;

    @BeforeEach
    void setUp(){

        //Prepare request Dto
        employeeRequestDto = new EmployeeRequestDto(
                "Sachin",
                888888,
                "Cricket",
                "sachin@gmail.com"
        );

        employee = new Employee();
        employee.setId(10L);
        employee.setName("Sachin");
        employee.setSalary(888888);
        employee.setDepartment("Cricket");
        employee.setEmail("sachin@gmail.com");

        //Prepare response Dto
        employeeResponseDto = new EmployeeResponseDto(
                10L,
                "Sachin",
                888888,
                "Cricket",
                "sachin@gmail.com"
        );
    }

    @Test
    public void testAddEmployee_Success() {

        //Mock service inner method calls
        when(employeeRepository.existsByEmail(employeeRequestDto.getEmail())).thenReturn(false);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(modelMapper.map(employeeRequestDto, Employee.class)).thenReturn(employee);
        when(modelMapper.map(employee, EmployeeResponseDto.class)).thenReturn(employeeResponseDto);

        //Perform request
        EmployeeResponseDto result = employeeService.addEmployee(employeeRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10);
        assertThat(result.getName()).isEqualTo("Sachin");

        //Verify the interactions
        verify(employeeRepository).existsByEmail(employeeRequestDto.getEmail());
        verify(employeeRepository).save(employee);
        verify(modelMapper).map(employeeRequestDto, Employee.class);
        verify(modelMapper).map(employee, EmployeeResponseDto.class);

    }

    @Test
    public void testAddEmployee_ServiceThrowsException(){

        //Mock service inner method calls
        when(employeeRepository.existsByEmail(employeeRequestDto.getEmail())).thenReturn(true);

        //Perform request
        assertThatThrownBy(() -> employeeService.addEmployee(employeeRequestDto))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("already exists");

        //Verify the interactions
        verify(employeeRepository).existsByEmail(employeeRequestDto.getEmail());
        verify(employeeRepository, never()).save(employee);
    }


}
