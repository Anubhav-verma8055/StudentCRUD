package com.example.school.service;

import com.example.school.model.Attendance;
import com.example.school.model.Student;
import com.example.school.model.Teacher;
import com.example.school.repository.AttendanceRepository;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AttendanceService {


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;
    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }
    public Attendance  markAttendance(Long studentId, String subject) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student record is not present"));

        Teacher teacher = teacherRepository.findById(student.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found for ID: " + student.getTeacherId()));


        Attendance attendance = new Attendance();
        attendance.setTeacherId(student.getTeacherId());
        attendance.setStudentId(studentId);
        attendance.setStudentname(student.getName());
        attendance.setTeachername(teacher.getName());
        attendance.setDate(LocalDate.now());
        attendance.setSubject(teacher.getSubject());
        attendance.setStudentPresent(true);

        return attendanceRepository.save(attendance);

    }

    public Attendance updateAttendance(Long studentId,String subject,Long teacherId,Long attendanceId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student record is not present"));

        Optional<Attendance> update = attendanceRepository.findById(attendanceId);

        if (update.isEmpty()) {
            throw new RuntimeException("Attendance record not found for ID: " + attendanceId);
        }

       if(student.getTeacherId().equals(teacherId)) {
           Attendance attendance = update.get();
           attendance.setStudentId(studentId);
           attendance.setStudentPresent(true);
           return attendanceRepository.save(attendance);
       }
       else {
           throw new RuntimeException("Not authorized to mark attendance");
       }
    }
}
