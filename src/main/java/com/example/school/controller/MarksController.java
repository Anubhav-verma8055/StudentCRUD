package com.example.school.controller;

import com.example.school.model.Marks;
import com.example.school.repository.MarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marks")
public class MarksController {
    @Autowired
    private MarksRepository marksRepository;

    @GetMapping("/student/{studentId}")
    public List<Marks> getMarksByStudentId(@PathVariable Long studentId) {
        return marksRepository.findByStudentId(studentId);
    }

    @PostMapping
    public Marks createMarks(@RequestBody Marks marks) {
        return marksRepository.save(marks);
    }

    @PutMapping("/{id}")
    public Marks updateMarks(@PathVariable Long id, @RequestBody Marks marks) {
        marks.setId(id); // Ensure the correct ID is updated
        return marksRepository.save(marks);
    }

    @DeleteMapping("/{id}")
    public void deleteMarks(@PathVariable Long id) {
        if (!marksRepository.existsById(id)) {
            throw new RuntimeException("Marks not found with ID: " + id);
        }
        marksRepository.deleteById(id);
    }


}
