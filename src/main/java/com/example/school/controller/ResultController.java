package com.example.school.controller;

import com.example.school.dto.StudentResultResponse;
import com.example.school.service.PaidResult;
import com.example.school.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/results")
public class ResultController {
    @Autowired
    private PaidResult paidService;

    @Autowired
    private ResultService resultService;

    @GetMapping("/student/{studentId}")
    public StudentResultResponse getStudentResult(@PathVariable Long studentId) {
        return resultService.generateStudentResult(studentId);
    }

    // Endpoint to get all student results
    @GetMapping
    public List<StudentResultResponse> getAllStudentResults() {
        // Fetch the student results from the service and return them directly
        return resultService.getStudentResults();
    }

    @GetMapping("/paidstudent/{month}")
    public List<StudentResultResponse> getAllStudentResult(@PathVariable String month) {
        return paidService.generatePaidStudentResult(month); }
}


