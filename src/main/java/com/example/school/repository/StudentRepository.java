package com.example.school.repository;

import com.example.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByTeacherId(Long teacherId);

    Optional<Student> findByEmail(String email);

   // Long getStudentIdFromEmail(String email);
    // List<Student>  findByStudentId(Long studentId);// Fetch students for a specific teacher
}
