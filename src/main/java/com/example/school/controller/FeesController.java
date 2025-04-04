package com.example.school.controller;

import com.example.school.model.Fees;
import com.example.school.repository.FeesRepository;
import com.example.school.service.PaidResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fees")
public class FeesController {
    @Autowired
    private FeesRepository feesRepository;

    @Autowired
    private PaidResult paidResult;

    @GetMapping("/showall")
    public List<Fees> getFeesByStudentId() {
        return feesRepository.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<Fees> getFeesByStudentId(@PathVariable Long studentId) {
        return feesRepository.findByStudentId(studentId);
    }

    @PostMapping
    public Fees createFees(@RequestBody Fees fees) {
        return feesRepository.save(fees);
    }

    @PutMapping("/{id}")
    public Fees updateFees(@PathVariable Long id, @RequestBody Fees fees) {
        fees.setId(id);
        return feesRepository.save(fees);
    }

    @DeleteMapping("/{id}")
    public void deleteFees(@PathVariable Long id) {
        if (!feesRepository.existsById(id)) {
            throw new RuntimeException("Fees not found with ID: " + id);
        }
        feesRepository.deleteById(id);
    }

//    @PostMapping("/student/monthFeesDetails")
//    public List<Map<String,Object>> getRequestedFeesDetails(@RequestBody List<Long> studentIds, @RequestBody List<String> months ) {
//        return paidResult.getRequestedFeesDetails(studentIds,months);
//    }
}
