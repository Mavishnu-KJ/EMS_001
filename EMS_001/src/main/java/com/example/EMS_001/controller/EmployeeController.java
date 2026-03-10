package com.example.EMS_001.controller;


import com.example.EMS_001.model.dto.EmployeeRequestDto;
import com.example.EMS_001.model.dto.EmployeeResponseDto;
import com.example.EMS_001.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @PostMapping("/addEmployee")
    ResponseEntity<EmployeeResponseDto> addEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequestDto){
        logger.info("addEmployee, employeeRequestDto is {}", employeeRequestDto);

        EmployeeResponseDto employeeAdded = employeeService.addEmployee(employeeRequestDto);
        logger.info("addEmployee, employeeAdded is {}", employeeAdded);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/employees/{id}")
                .buildAndExpand(employeeAdded.getId())
                .toUri();
        logger.info("addEmployee, location is {}", location);

        return ResponseEntity.created(location).body(employeeAdded);
    }


}
