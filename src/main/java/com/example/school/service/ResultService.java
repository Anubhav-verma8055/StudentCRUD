package com.example.school.service;

import com.example.school.dto.StudentResultResponse;
import com.example.school.model.*;
import com.example.school.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private FeesRepository feesRepository;

    @Autowired
    private StudentResultRepository studentResultRepository;


    public List<StudentResultResponse> getStudentResults() {
        List<StudentResult> results = studentResultRepository.findAll();

        return results.stream()
                .map(result -> mapToDTO(result))
                .collect(Collectors.toList());
    }


    private StudentResultResponse mapToDTO(StudentResult result) {
        StudentResultResponse response = new StudentResultResponse();
        response.setStudentId(result.getStudentId());
        response.setStudentName(result.getStudentName());
        response.setStudentClass(result.getStudentClass());
        response.setTeacherName(result.getStudentName());

        response.setHindi(result.getHindi());
        response.setEnglish(result.getEnglish());
        response.setMaths(result.getMaths());
        response.setScience(result.getScience());
        response.setPolitics(result.getPolitics());
        response.setPhysicalEducation(result.getPhysicalEducation());
        response.setTotalMarks(result.getTotalMarks());
        response.setTotalMaxMarks(result.getTotalMaxMarks());
        response.setPercentage(result.getPercentage());
        response.setGrade(result.getGrade());
        response.setMonth(result.getMonthFee());
        return response;
    }

    public StudentResultResponse generateStudentResult(Long studentId) {

        // Fetch student details
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        // Fetch marks
        Marks marks = marksRepository.findByStudentId(studentId).get(0);

        // Fetch teacher details
        Teacher teacher = teacherRepository.findById(student.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + student.getTeacherId()));

        // Fetch fees details
        List<Fees> feesList = feesRepository.findByStudentId(studentId);
        if (feesList.isEmpty()) {
            throw new RuntimeException("Fees not found for student ID: " + studentId);
        }
        Fees fees = feesList.get(0);

        // Calculate total marks, percentage, and grade
        int totalMarks = marks.getHindi() + marks.getEnglish() + marks.getMaths() +
                marks.getScience() + marks.getPolitics() + marks.getPhysicalEducation();
        int totalMaxMarks = 600; // Assume each subject is out of 100
        double percentage = (totalMarks / (double) totalMaxMarks) * 100;
        String grade = calculateGrade(percentage);

        // Create and populate StudentResult entity
        StudentResult studentResult = new StudentResult();
        studentResult.setStudentId(student.getId());
        studentResult.setStudentName(student.getName());
        studentResult.setStudentClass(student.getStudentClass());
        studentResult.setHindi(marks.getHindi());
        studentResult.setEnglish(marks.getEnglish());
        studentResult.setMaths(marks.getMaths());
        studentResult.setScience(marks.getScience());
        studentResult.setPolitics(marks.getPolitics());
        studentResult.setPhysicalEducation(marks.getPhysicalEducation());
        studentResult.setTotalMarks(totalMarks);
        studentResult.setTotalMaxMarks(totalMaxMarks);
        studentResult.setPercentage(percentage);
        studentResult.setGrade(grade);
        studentResult.setMonth(fees.getMonth());
        studentResult.setMonthFee(fees.getMonth());

        // Save the entity before mapping
        studentResult = studentResultRepository.save(studentResult);

        // Map and return the response DTO
        return mapToDTO(studentResult);
    }
//Generate studentResult whose fees are apid for given month
    public StudentResultResponse generateStudentResultForMonth(Long studentId, String month) {
        // Find the student entity
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        // Find the teacher assigned to the student
        Teacher teacher = teacherRepository.findById(student.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with ID: " + student.getTeacherId()));

        Marks marks = marksRepository.findByStudentId(studentId).get(0);

        // Get the fees and filter by month
        Fees fees = feesRepository.findByStudentIdAndMonth(studentId, month)
                .orElseThrow(() -> new RuntimeException("Fees not found for student ID: " + studentId + " for month: " + month));

        // Calculate the total marks, percentage, and grade
        int totalMarks = marks.getHindi() + marks.getEnglish() + marks.getMaths() +
                marks.getScience() + marks.getPolitics() + marks.getPhysicalEducation();
        int totalMaxMarks = 600; // Assume each subject is out of 100
        double percentage = (totalMarks / (double) totalMaxMarks) * 100;
        String grade = calculateGrade(percentage);

        // Create the StudentResultResponse to be sent
        StudentResultResponse response = new StudentResultResponse();
        response.setStudentId(student.getId());
        response.setStudentName(student.getName());
        response.setStudentClass(student.getStudentClass());
        response.setTeacherName(teacher.getName());
        response.setTeacherSpeciality(teacher.getSpeciality());
        response.setHindi(marks.getHindi());
        response.setEnglish(marks.getEnglish());
        response.setMaths(marks.getMaths());
        response.setScience(marks.getScience());
        response.setPolitics(marks.getPolitics());
        response.setPhysicalEducation(marks.getPhysicalEducation());
        response.setTotalMarks(totalMarks);
        response.setTotalMaxMarks(totalMaxMarks);
        response.setPercentage(percentage);
        response.setGrade(grade);
        response.setMonth(month);

        return response;
    }

    private String calculateGrade(Double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        return "F";
    }



//    public List<StudentResultResponse> generatePaidStudentResult(String month) {
//        List<Fees> allFees = feesRepository.findAll();
//
//
////        List<Fees> paidFees = new ArrayList<>();
////        for (Fees fee : allFees) {
////            if (fee.getMonth().equalsIgnoreCase(month) && fee.isSubmitted()) {
////                paidFees.add(fee);
////            }
//        //  }
//        List<Fees> paidFees = allFees.stream()
//                .filter(fee -> fee.getMonth().equalsIgnoreCase(month) && fee.isSubmitted())
//                .toList();
//        List<StudentResultResponse> results = new ArrayList<>();
//        // for (Fees fee : paidFees) {
//        return paidFees.stream()
//                .map(fee -> {
//                    return generateStudentResult(fee.getStudentId());
//
//                }).toList();

    }

