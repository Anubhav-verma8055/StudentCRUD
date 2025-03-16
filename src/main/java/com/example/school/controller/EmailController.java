package com.example.school.controller;

import com.example.school.service.EmailService;
import com.example.school.service.SendEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SendEmailService sendEmailService;



    @PostMapping("/sendResults")
    public ResponseEntity<String> sendResultsEmail(
            @RequestParam List<String> studentEmails,  // List of student emails
            @RequestParam String subject,              // Subject for the email
            @RequestParam String month,                // Month for the results
            @RequestParam(required = false) List<String> carbonCopy,  // CC recipients
            @RequestParam(required = false) List<String> blindCarbonCopy // BCC recipients
    ) throws MessagingException, IOException {

        return  sendEmailService.sendStudentResultsEmail(studentEmails, subject, month, carbonCopy, blindCarbonCopy);
    }

    // API for sending notices
    @PostMapping("/sendNotice")
    public String sendNotice(
            @RequestParam List<String> toEmails,
            @RequestParam String subject,
            @RequestParam(required = false) List<String> carbonCopy,
            @RequestParam(required = false) List<String> blindCarbonCopy,
            @RequestParam(required = false) MultipartFile attachment,
            @RequestParam String templateType) throws MessagingException, IOException {

        return sendEmailService.sendEmail(toEmails, subject, carbonCopy, blindCarbonCopy, attachment, templateType);
    }

}

