package com.example.school.repository;

import com.example.school.model.Fees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FeesRepository extends JpaRepository<Fees, Long> {
    List<Fees> findByStudentId(Long studentId);

    List<Fees> findByMonthAndSubmitted(String month, boolean submitted);
}
