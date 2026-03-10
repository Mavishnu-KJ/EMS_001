package com.example.EMS_001.service;

import com.example.EMS_001.model.dto.EmployeeRequestDto;
import com.example.EMS_001.model.dto.EmployeeResponseDto;

public interface EmployeeService {

    EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto);

}
