package com.example.EMS_001.controller;

import com.example.EMS_001.exceptions.DuplicateEmailException;
import com.example.EMS_001.model.dto.EmployeeRequestDto;
import com.example.EMS_001.model.dto.EmployeeResponseDto;
import com.example.EMS_001.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    EmployeeService employeeService;

    @Test
    public void testAddEmployee_Success() throws Exception{
        //Prepare request dto
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(
                "Sachin",
                888888,
                "Cricket",
                "sachin@gmail.com"
        );

        //Prepare response dto
        EmployeeResponseDto employeeResponseDto = new EmployeeResponseDto(
                10L,
                "Sachin",
                888888,
                "Cricket",
                "sachin@gmail.com"
        );

        //Mock service behavior
        when(employeeService.addEmployee(any(EmployeeRequestDto.class))).thenReturn(employeeResponseDto);

        //Perform POST request
        mockMvc.perform(post("/api/employees/addEmployee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("/api/employees/10")))
                .andExpect(jsonPath("$.id").value("10"))
                .andExpect(jsonPath("$.name").value("Sachin"));

        //Verify the service was called once
        verify(employeeService, times(1)).addEmployee(any(EmployeeRequestDto.class));

    }

    @Test
    public void testAddEmployee_ValidationFailure() throws Exception{

        //Prepare request dto
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(
                "",
                888888,
                "Cricket",
                "sachin@gmail.com"
        );

        //Perform POST request
        mockMvc.perform(post("/api/employees/addEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.shortSummaryMessage").value("Validation Failed"))
                .andExpect(jsonPath("$.errorList").isArray())
                .andExpect(jsonPath("$.errorList").isNotEmpty())
                .andExpect(jsonPath("$.errorList[0]").value(containsString("must not be blank")));

        //Verify the service was never called
        verify(employeeService, never()).addEmployee(employeeRequestDto);
    }

    @Test
    public void testAddEmployee_ServiceThrowsException() throws Exception{

        //Prepare request Dto
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(
                "Sachin",
                888888,
                "Cricket",
                "sachin@gmail.com"
        );

        //Mock service behavior
        when(employeeService.addEmployee(any(EmployeeRequestDto.class))).thenThrow(new DuplicateEmailException());

        //Perform POST request
        mockMvc.perform(post("/api/employees/addEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.shortSummaryMessage").value("Duplicate Email"))
                .andExpect(jsonPath("$.errorList").isArray())
                .andExpect(jsonPath("$.errorList").isNotEmpty())
                .andExpect(jsonPath("$.errorList[0]").value(containsString("already exists")));

        //Verify the service was called once
        verify(employeeService, times(1)).addEmployee(any(EmployeeRequestDto.class));

    }

}
