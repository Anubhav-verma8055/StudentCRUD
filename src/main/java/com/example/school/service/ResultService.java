package com.example.school.service;

import com.example.school.dto.StudentResultResponse;
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

import java.lang.reflect.Field;
import java.util.*;

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


    public StudentResultResponse generateStudentResult(Long studentId) {


        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        // Fees fees = feesRepository.findByStudentId(studentId).get(0);
        // .orElseYhrow(() -> new RuntimeException("Student not found with ID: " + studentId));
        List<Fees> feesList = feesRepository.findByStudentId(studentId);
        if (feesList.isEmpty()) {
            throw new RuntimeException("Marks not found for student ID: " + studentId);
        }
        Fees fees = feesList.get(0);


        Marks marks = marksRepository.findByStudentId(studentId).get(0);


        Teacher teacher = teacherRepository.findById(student.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found for ID: " + student.getTeacherId()));


        int totalMarks = marks.getHindi() + marks.getEnglish() + marks.getMaths() +
                marks.getScience() + marks.getPolitics() + marks.getPhysicalEducation();
        int totalMaxMarks = 600; // Assume each subject is out of 100
        double percentage = (totalMarks / (double) totalMaxMarks) * 100;

        // Determine grade
        String grade = calculateGrade(percentage);

        // Create and return the response
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
        response.setMonth(fees.getMonth());

        return response;
    }

    private String calculateGrade(Double percentage) {
        if (percentage >= 90) return "A+";
        if (percentage >= 80) return "A";
        if (percentage >= 70) return "B";
        if (percentage >= 60) return "C";
        return "F";
    }

    public Map<String, Object> getRequirementsBasedMarks(Long studentId, List<String> subjects) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        Teacher teacher = teacherRepository.findById(student.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found for ID: " + student.getTeacherId()));


        Optional<Marks> marksOpt = marksRepository.findByStudentId(studentId).stream().findFirst();
        if (marksOpt.isEmpty()) {
            throw new RuntimeException("Marks not found for student ID: " + studentId);
        }
        Marks marks = marksOpt.get();

        //construct response dynamically
        Map<String, Object> response = new HashMap<>();
        response.put("studentId", student.getId());
        response.put("studentName", student.getName());
        response.put("teacherName", teacher != null ? teacher.getName() : "Unknown");

        Map<String, Integer> marksMap = new HashMap<>();
        if (subjects == null || subjects.isEmpty()) {
            marksMap.put("hindi", marks.getHindi());
            marksMap.put("english", marks.getEnglish());
            marksMap.put("maths", marks.getMaths());
            marksMap.put("science", marks.getScience());
            marksMap.put("politics", marks.getPolitics());
            marksMap.put("physicalEducation", marks.getPhysicalEducation());
        } else {
            for (String subject : subjects) {
                switch (subject.toLowerCase()) {
                    case "hindi":
                        marksMap.put("hindi", marks.getHindi());
                        break;

                    case "english":
                        marksMap.put("english", marks.getEnglish());
                        break;

                    case "maths":
                        marksMap.put("maths", marks.getMaths());
                        break;
                    case "science":
                        marksMap.put("science", marks.getScience());
                        break;
                    case "politics":
                        marksMap.put("politics", marks.getPolitics());
                        break;
                    case "physicaleducation":
                        marksMap.put("physicalEducation", marks.getPhysicalEducation());
                        break;
                    default:
                        throw new RuntimeException("Invalid subject: " + subject);
                }
            }

        }

        response.put("marks", marksMap);
        return response;
    }

    public List<Map<String, Object>> getRequestedMarks(Long studentId, List<String> subjects) {

        List<Map<String, Object>> resultList = new ArrayList<>();
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        Teacher teacher = teacherRepository.findById(student.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found for ID: " + student.getTeacherId()));


        Optional<Marks> marksOpt = marksRepository.findByStudentId(studentId).stream().findFirst();
        if (marksOpt.isEmpty()) {
            throw new RuntimeException("Marks not found for student ID: " + studentId);
        }
        Marks marks = marksOpt.get();

        //construct response dynamically
        Map<String, Object> response = new HashMap<>();
        response.put("studentId", student.getName());
        response.put("studentName", student.getName());
        response.put("teacherName", teacher != null ? teacher.getName() : "Unknown");

        Map<String, Integer> marksMap = new HashMap<>();
        if (subjects == null || subjects.isEmpty()) {
            for (Field field : Marks.class.getDeclaredFields()) {
                if (field.getType().equals(int.class)) { //assume only for int fields in marks
                    try {
                        field.setAccessible(true);
                        marksMap.put(field.getName(), field.getInt(marks));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error accessing field: " + field.getName(), e);
                    }
                } else {
                    // Fetch only requested subjects dynamically
                    for (String subject : subjects) {
                        try {
                            Field fields = Marks.class.getDeclaredField(subject.toLowerCase());
                            fields.setAccessible(true);
                            marksMap.put(subject.toLowerCase(), fields.getInt(marks));
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new RuntimeException("Invalid subject: " + subject);
                        }
                    }
                }


            }
        }
        response.put("marks", marksMap);
        resultList.add(response);
        return resultList;
    }
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




