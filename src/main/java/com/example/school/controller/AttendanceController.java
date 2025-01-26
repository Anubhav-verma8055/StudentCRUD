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

    // Get attendance records for a student
     @GetMapping("/student/{studentId}")
     public List<Attendance> getAttendanceByStudent(@PathVariable Long studentId) {
        List<Attendance> attendanceList = attendanceRepository.getAttendanceByStudentId(studentId);
        return attendanceList;
     }

     //Get attendance records for a teacher
    @GetMapping("/teacher/{teacherId}")
    public List<Attendance> getAttendanceByTeacher(@PathVariable Long teacherId) {
        List<Attendance> attendanceList = attendanceRepository.getAttendanceByTeacherId(teacherId);
        return attendanceList;
    }

    //Mark attendance for a student
    @GetMapping("/student/{studentId}/{subject}")
    public Attendance markAttendance(@PathVariable  Long studentId, @PathVariable String subject) {
        return attendanceService.markAttendance(studentId,subject);
    }

      //Update attendance record
    @PutMapping("/update/{studentId}/{subject}/{teacherId}/{attendanceId}")
    public Attendance updateAttendance(@PathVariable  Long studentId, @PathVariable String subject,@PathVariable Long teacherId,@PathVariable Long attendanceId) {
        return attendanceService.updateAttendance(studentId,subject,teacherId,attendanceId);
    }

    //Delete an attendance record
    @DeleteMapping("/delete/{attendanceId}")
    public void deleteAttendance(@PathVariable Long attendanceId) {
        if (!attendanceRepository.existsById(attendanceId)) {
            throw new RuntimeException("Attendance record not found for ID: " + attendanceId);
        }
        attendanceRepository.deleteById(attendanceId);
    }
}
