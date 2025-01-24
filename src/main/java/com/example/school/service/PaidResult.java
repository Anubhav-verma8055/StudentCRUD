package com.example.school.service;

import com.example.school.dto.StudentResultResponse;
import com.example.school.model.Fees;
import com.example.school.repository.FeesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaidResult {
   @Autowired
    private FeesRepository feesRepository;

   @Autowired
    private ResultService resultService;


   public List<StudentResultResponse> generatePaidStudentResult(String month) {
       List<Fees> allstudentList = feesRepository.findAll();
       List<StudentResultResponse> marksheet = new ArrayList<>();
       for(Fees paidFeeStudents : allstudentList) {
           if(paidFeeStudents.getMonth().equals(month) && paidFeeStudents.isSubmitted())
           {
               marksheet.add(resultService.generateStudentResult(paidFeeStudents.getStudentId()));
           }
       }
       return marksheet;
   }

}
