package com.example.school.controller;

import com.example.school.dto.StudentsAllDetails;
import com.example.school.service.GeneralStudentsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generalstudentsdetails")
public class StudentsAllDetailsController {

    @Autowired
    private GeneralStudentsDetailService gs;

    @GetMapping("/student/{studentId}")
    public StudentsAllDetails getallDetails(@PathVariable long studentId) {
        return gs.generateallDetails(studentId);
    }
}
