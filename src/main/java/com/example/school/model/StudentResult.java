package com.example.school.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_result")
public class StudentResult {

    @Id
    private Long id;

    private Long studentId;
    private String studentName;
    private String studentClass;
    private Integer hindi;
    private Integer english;
    private Integer maths;
    private Integer science;
    private Integer politics;
    private Integer physicalEducation;
    private Integer totalMarks;
    private Integer totalMaxMarks;
    private Double percentage;
    private String grade;
    private String month;
    private String monthFee;

    public Integer getTotalMaxMarks() {
        return totalMaxMarks;
    }

    public void setTotalMaxMarks(Integer totalMaxMarks) {
        this.totalMaxMarks = totalMaxMarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public Integer getHindi() {
        return hindi;
    }

    public void setHindi(Integer hindi) {
        this.hindi = hindi;
    }

    public Integer getEnglish() {
        return english;
    }

    public void setEnglish(Integer english) {
        this.english = english;
    }

    public Integer getMaths() {
        return maths;
    }

    public void setMaths(Integer maths) {
        this.maths = maths;
    }

    public Integer getScience() {
        return science;
    }

    public void setScience(Integer science) {
        this.science = science;
    }

    public Integer getPolitics() {
        return politics;
    }

    public void setPolitics(Integer politics) {
        this.politics = politics;
    }

    public Integer getPhysicalEducation() {
        return physicalEducation;
    }

    public void setPhysicalEducation(Integer physicalEducation) {
        this.physicalEducation = physicalEducation;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthFee() {
        return monthFee;
    }

    public void setMonthFee(String monthFee) {
        this.monthFee = monthFee;
    }


}
