package com.example.school.repository;

import com.example.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByTeacherId(Long teacherId); // Fetch students for a specific teacher
}
