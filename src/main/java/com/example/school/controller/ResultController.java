package com.example.school.controller;

import com.example.school.dto.StudentResultResponse;
import com.example.school.model.Marks;
import com.example.school.repository.MarksRepository;
import com.example.school.service.PaidResult;
import com.example.school.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/results")
public class ResultController {
    @Autowired
    private PaidResult paidService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private MarksRepository marksRepository;

    @GetMapping("/student/{studentId}")
    public StudentResultResponse getStudentResult(@PathVariable Long studentId) {
        return resultService.generateStudentResult(studentId);
    }

    @GetMapping("/paidstudent/{month}")
    public List<StudentResultResponse> getAllStudentResult(@PathVariable String month) {
        return paidService.generatePaidStudentResult(month); }

    @PostMapping("/getRequirementsNeeds/{studentId}")
    public Map<String,Object> getRequirementsBasedMarks(@PathVariable Long studentId, @RequestBody List<String> subjects) {
     return resultService.getRequirementsBasedMarks(studentId,subjects);
    }

    //more optimize way to get the fields value of marks - using java reflection
    @PostMapping("/optimize/getRequestedMarks/{studentId}")
    public List<Map<String,Object>> getRequestedMarks(@PathVariable Long studentId,@RequestBody List<String> subjects) {
        return resultService.getRequestedMarks(studentId,subjects);
    }

    //utilising the studentStatusResponse dto to get the required marks of excluded subjects passed as list
    @PostMapping("/student/exclude-subjects")
    public StudentResultResponse getFilteredStudentMarks(@RequestBody Long studentId,@RequestBody List<String> excludeSubjects) {
        return resultService.getFilteredStudentMarks(studentId,excludeSubjects);
    }

}


