package com.example.school.controller;

import com.example.school.model.Lecture;
import com.example.school.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    @Autowired
    private LectureService lectureService;

    @PostMapping("/addLecture")
    public Lecture addLecture(@RequestBody Lecture lecture) {
        return lectureService.addLecture(lecture);
    }

    @GetMapping("/{lectureId}")
    public Lecture getLectureById(@PathVariable Long lectureId) {
        return lectureService.getLectureById(lectureId);
    }

    @PutMapping("/{lectureId}")
    public Lecture updateLecture(
            @PathVariable Long lectureId,
            @RequestBody Lecture updatedLecture) {
        return lectureService.updateLecture(lectureId, updatedLecture);
    }

    // Get lectures by teacher ID
    @GetMapping("/teacher/{teacherId}")
    public List<Lecture> getLecturesByTeacherId(@PathVariable Long teacherId) {
        return lectureService.getLecturesByTeacherId(teacherId);
    }

    // Get lectures by subject
    @GetMapping("/subject/{subject}")
    public List<Lecture> getLecturesBySubject(@PathVariable String subject) {
        return lectureService.getLecturesBySubject(subject);
    }

    @GetMapping
    public List<Lecture> getAllLectures() {
        return lectureService.getAllLectures();
    }

    // Delete lecture
    @DeleteMapping("/{lectureId}")
    public String deleteLecture(@PathVariable Long lectureId) {
        lectureService.deleteLecture(lectureId);
        return "Lecture deleted successfully.";
    }
}
