package com.example.school.repository;

import com.example.school.model.Fees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//import java.lang.ScopedValue;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeesRepository extends JpaRepository<Fees, Long> {
    List<Fees> findByStudentId(Long studentId);
    //Optional<Fees> getBystudentId(Long studentId);

    List<Fees> findByMonthAndSubmitted(String month, boolean submitted);

    Optional<Fees> findByStudentIdAndMonth(Long studentId, String month);
}
