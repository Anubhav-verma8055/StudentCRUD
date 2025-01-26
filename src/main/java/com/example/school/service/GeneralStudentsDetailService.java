package com.example.school.service;

import com.example.school.dto.StudentsAllDetails;
import com.example.school.model.Fees;
import com.example.school.model.Marks;
import com.example.school.model.Student;
import com.example.school.model.Teacher;
import com.example.school.repository.FeesRepository;
import com.example.school.repository.MarksRepository;
import com.example.school.repository.StudentRepository;
import com.example.school.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class GeneralStudentsDetailService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private FeesRepository feesRepository;

    public StudentsAllDetails generateallDetails(long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student record is not present"));

        Teacher teacher = teacherRepository.findById(student.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found for ID: " + student.getTeacherId()));

        Marks marks = marksRepository.findByStudentId(studentId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Marks not found for student ID: " + studentId));

        Fees fees = feesRepository.findByStudentId(studentId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Fees not found for student ID: " + studentId));

        StudentsAllDetails detail= new StudentsAllDetails();
        detail.setId(student.getId());

        detail.setStudent_name(student.getName());
        detail.setStudent_email(student.getEmail());
        detail.setStudent_mobile(student.getMobile());
        detail.setStudent_address(student.getAddress());
        detail.setStudentClass(student.getStudentClass());
        detail.setTeacherId(student.getTeacherId());


        detail.setTeacher_name(teacher.getName());
        detail.setTeacher_address(teacher.getAddress());
        detail.setTeacher_mobile(teacher.getMobile());
        detail.setTeacher_email(teacher.getEmail());
        detail.setExperience(teacher.getExperience());
        detail.setSpeciality(teacher.getSpeciality());

        detail.setHindi(marks.getHindi());
        detail.setEnglish(marks.getEnglish());
        detail.setMaths(marks.getMaths());
        detail.setScience(marks.getScience());
        detail.setPolitics(marks.getPolitics());
        detail.setPhysicalEducation(marks.getPhysicalEducation());

        detail.setMonth(fees.getMonth());
        detail.setSubmitted(fees.isSubmitted());
        return detail;
    }
}
