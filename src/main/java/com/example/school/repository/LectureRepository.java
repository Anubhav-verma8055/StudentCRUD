package com.example.school.repository;

import com.example.school.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture,Long> {
    List<Lecture> findByTeacherId(Long teacherId);
    List<Lecture> findBySubject(String subject);
}
