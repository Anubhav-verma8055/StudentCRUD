package com.example.school.service;

import com.example.school.dto.StudentResultResponse;
import org.springframework.web.multipart.MultipartFile;
import jakarta.mail.MessagingException;
import com.example.school.repository.StudentRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
;
    public void sendEmailWithAttachment(
            String toEmail,
            String subject,
            String body,
            List<String> carbonCopy,
            List<String> blindCarbonCopy,
            byte[] pdfAttachment) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set the email fields
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body);

        if (carbonCopy != null && !carbonCopy.isEmpty()) {
            helper.setCc(carbonCopy.toArray(new String[0]));
        }
        if (blindCarbonCopy != null && !blindCarbonCopy.isEmpty()) {
            helper.setBcc(blindCarbonCopy.toArray(new String[0]));
        }

        // Attach the PDF
        helper.addAttachment("student_result.pdf", new ByteArrayDataSource(pdfAttachment, "application/pdf"));

        // Send the email
        mailSender.send(message);
    }

}
