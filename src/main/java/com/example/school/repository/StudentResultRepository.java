package com.example.school.repository;

import com.example.school.dto.StudentResultResponse;
import com.example.school.model.StudentResult;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface StudentResultRepository extends JpaRepository<StudentResult, Long> {
    Optional<StudentResultResponse> findByStudentIdAndMonth(Long studentId, String month);
}
