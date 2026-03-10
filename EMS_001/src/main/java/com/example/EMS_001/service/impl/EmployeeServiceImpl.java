package com.example.EMS_001.service.impl;

import com.example.EMS_001.exceptions.DuplicateEmailException;
import com.example.EMS_001.model.dto.EmployeeRequestDto;
import com.example.EMS_001.model.dto.EmployeeResponseDto;
import com.example.EMS_001.model.entity.Employee;
import com.example.EMS_001.repository.EmployeeRepository;
import com.example.EMS_001.service.EmployeeService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper){
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Override
    @Transactional
    public EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto){
        logger.info("addEmployee, employeeRequestDto is {}", employeeRequestDto);

        //Validate emailId
        if(employeeRequestDto != null && !employeeRequestDto.getEmail().isBlank() && employeeRepository.existsByEmail(employeeRequestDto.getEmail())){
            throw new DuplicateEmailException(employeeRequestDto.getEmail());
        }

        //map request dto to employee
        Employee employee = modelMapper.map(employeeRequestDto, Employee.class);
        logger.info("addEmployee, employee is {}", employee);

        //save
        Employee employeeSaved = employeeRepository.save(employee);
        logger.info("addEmployee, employeeSaved is {}", employeeSaved);

        //map employee to employee response dto
        EmployeeResponseDto employeeResponseDto = modelMapper.map(employeeSaved, EmployeeResponseDto.class);
        logger.info("addEmployee, employeeResponseDto is {}", employeeResponseDto);

        return employeeResponseDto;
    }




}
