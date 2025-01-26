package com.example.school.repository;

import com.example.school.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
 List<Attendance> getAttendanceByStudentId(Long studentId);
 List<Attendance> getAttendanceByTeacherId(Long teacherId);
}
