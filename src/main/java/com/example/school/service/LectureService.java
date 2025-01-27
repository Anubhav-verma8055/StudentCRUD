package com.example.school.service;

import com.example.school.model.Lecture;
import com.example.school.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureService {
    @Autowired
    private LectureRepository lectureRepository;

    public LectureService(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Lecture addLecture(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    // Get lectures by teacher ID
    public List<Lecture> getLecturesByTeacherId(Long teacherId) {
        return lectureRepository.findByTeacherId(teacherId);
    }

    // Get lecture by ID
    public Lecture getLectureById(Long lectureId) {
        return lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found for ID: " + lectureId));
    }

    // Update lecture details
    public Lecture updateLecture(Long lectureId, Lecture updatedLecture) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found for ID: " + lectureId));
        lecture.setTeacherId(updatedLecture.getTeacherId());
        lecture.setTeacherName(updatedLecture.getTeacherName());
        lecture.setRoomNo(updatedLecture.getRoomNo());
        lecture.setSubject(updatedLecture.getSubject());
        lecture.setSubstitution(updatedLecture.isSubstitution());
        return lectureRepository.save(lecture);
    }

    // Get lectures by subject
    public List<Lecture> getLecturesBySubject(String subject) {
        return lectureRepository.findBySubject(subject);
    }

    // Get all lectures
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    // Delete lecture
    public void deleteLecture(Long lectureId) {
        if (!lectureRepository.existsById(lectureId)) {
            throw new RuntimeException("Lecture not found for ID: " + lectureId);
        }
        lectureRepository.deleteById(lectureId);
    }



}
