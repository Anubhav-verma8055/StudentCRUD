package com.example.school.repository;

import com.schoolmanagement.model.StudentPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPaymentRepository extends JpaRepository<StudentPayment, Long> {
}