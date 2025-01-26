package com.example.school.controller;

import com.example.school.model.Attendance;
import com.example.school.repository.AttendanceRepository;
import com.example.school.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }
     @GetMapping("/allAttendance")
     public List<Attendance> getAllAttendance() {
         return attendanceRepository.findAll();
     }

    @GetMapping("/student/{studentId}/{subject}")
    public Attendance markAttendance(@PathVariable  Long studentId, @PathVariable String subject) {
        return attendanceService.markAttendance(studentId,subject);
    }

    @PutMapping("/update/{studentId}/{subject}/{teacherId}/{attendanceId}")
    public Attendance updateAttendance(@PathVariable  Long studentId, @PathVariable String subject,@PathVariable Long teacherId,@PathVariable Long attendanceId) {
        return attendanceService.updateAttendance(studentId,subject,teacherId,attendanceId);
    }
}
