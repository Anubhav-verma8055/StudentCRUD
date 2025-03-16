package com.example.school.service;

import com.example.school.dto.StudentResultResponse;
import com.example.school.repository.StudentResultRepository;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PDFGenerationService {

    @Autowired
    private StudentResultRepository studentResultRepository;

    public byte[] generateStudentResultPDF(Long studentId, String month) throws IOException {
        // Fetch student details
        StudentResultResponse studentResultResponse = studentResultRepository.findByStudentIdAndMonth(studentId, month)
                .orElseThrow(() -> new RuntimeException("No results found for student ID: " + studentId + " for month: " + month));


        // Prepare PDF document
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Title
        document.add(new Paragraph("Student Result Report")
                .setBold()
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("\n"));

        // Student Details
        document.add(new Paragraph("Student Name: " + studentResultResponse.getStudentName()));
        document.add(new Paragraph("Class: " + studentResultResponse.getStudentClass()));
        document.add(new Paragraph("Teacher Name: " + studentResultResponse.getTeacherName()));
        document.add(new Paragraph("Teacher Speciality: " + studentResultResponse.getTeacherSpeciality()));
        document.add(new Paragraph("Month: " + studentResultResponse.getMonth()));
        document.add(new Paragraph("Fee for the Month: " + studentResultResponse.getMonthFee()));

        document.add(new Paragraph("\n"));

        // Marks Table Setup
        Table table = new Table(new float[]{3, 3});
        table.setWidth(UnitValue.createPercentValue(100));

        // Table Headers
        table.addHeaderCell("Subject");
        table.addHeaderCell("Marks Obtained");

        // Fill Table with Data
        table.addCell("Hindi");
        table.addCell(String.valueOf(studentResultResponse.getHindi()));

        table.addCell("English");
        table.addCell(String.valueOf(studentResultResponse.getEnglish()));

        table.addCell("Maths");
        table.addCell(String.valueOf(studentResultResponse.getMaths()));

        table.addCell("Science");
        table.addCell(String.valueOf(studentResultResponse.getScience()));

        table.addCell("Politics");
        table.addCell(String.valueOf(studentResultResponse.getPolitics()));

        table.addCell("Physical Education");
        table.addCell(String.valueOf(studentResultResponse.getPhysicalEducation()));

        document.add(table);

        // Total Marks and Grade
        document.add(new Paragraph("\nTotal Marks: " + studentResultResponse.getTotalMarks() + "/" + studentResultResponse.getTotalMaxMarks()));
        document.add(new Paragraph("Percentage: " + studentResultResponse.getPercentage() + "%"));
        document.add(new Paragraph("Grade: " + studentResultResponse.getGrade()));

        document.close();
        return outputStream.toByteArray();

    }
}
