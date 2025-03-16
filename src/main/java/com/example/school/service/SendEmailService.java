package com.example.school.service;

import com.example.school.model.Student;
import com.example.school.repository.StudentRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SendEmailService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PDFGenerationService pdfGenerationService;

    @Autowired
    private JavaMailSender mailSender;

    public String sendEmail(List<String> toEmails, String subject,
                            List<String> carbonCopy, List<String> blindCarbonCopy, MultipartFile attachment,
                            String templateType) throws MessagingException, IOException {


        // Loop through the list of student emails
        for (String email : toEmails) {
            Optional<Student> studentOptional = studentRepository.findByEmail(email);
            if (studentOptional.isPresent()) {
                Student student = studentOptional.get();

                // Check which template to use based on the event type
                String emailBody = null;
                switch (templateType.toLowerCase()) {
                    case "cultural_fest":
                        emailBody = buildCulturalFestTemplate(student);
                        break;
                    case "sports_fest":
                        emailBody = buildSportsFestTemplate(student);
                        break;
                    case "cultural_event":
                        emailBody = buildCulturalEventTemplate(student);
                        break;
                    case "scientific_technical_fest":
                        emailBody = buildScientificTechnicalFestTemplate(student);
                        break;
                    case "hackathon":
                        emailBody = buildHackathonTemplate(student);
                        break;
                    default:
                        return "No template found for the specified event type.";
                }

                // Create the email message
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setSubject(subject);
                helper.setTo(email);

                // Add CC and BCC if provided
                if (carbonCopy != null && !carbonCopy.isEmpty()) {
                    helper.setCc(carbonCopy.toArray(new String[0]));
                }
                if (blindCarbonCopy != null && !blindCarbonCopy.isEmpty()) {
                    helper.setBcc(blindCarbonCopy.toArray(new String[0]));
                }

                // Set the email body content (HTML enabled)
                helper.setText(emailBody, true);

                // Attach file if present
                if (attachment != null && !attachment.isEmpty()) {
                    helper.addAttachment(attachment.getOriginalFilename(), attachment);
                }

                // Send the email
                mailSender.send(message);
            } else {
                throw new RuntimeException("No student found with email: " + email);
            }
        }

        return "Emails sent successfully!";
    }

    private String buildCulturalFestTemplate(Student student) {
        return "<h2>Dear " + student.getName() + ",</h2>"
                + "<p>We are excited to invite you to the <b>Intercollege Cultural Fest</b>!</p>"
                + "<p>Your participation is highly appreciated, and we look forward to your amazing performances.</p>"
                + "<p>Event Date: <b>March 25, 2025</b></p>"
                + "<p>Venue: <b>XYZ College Auditorium</b></p>"
                + "<p>For any queries, feel free to reach out to the event organizers.</p>"
                + "<br><hr><p>Best regards, <br> XYZ College Cultural Committee</p>";
    }

    private String buildSportsFestTemplate(Student student) {
        return "<h2>Dear " + student.getName() + ",</h2>"
                + "<p>Welcome to the <b>Intercollege Sports Fest</b>!</p>"
                + "<p>Get ready to showcase your athletic skills and represent your college in this exciting event.</p>"
                + "<p>Event Date: <b>April 10, 2025</b></p>"
                + "<p>Venue: <b>XYZ College Sports Ground</b></p>"
                + "<p>If you need any assistance, please contact the sports fest team.</p>"
                + "<br><hr><p>Best regards, <br> XYZ College Sports Committee</p>";
    }

    private String buildCulturalEventTemplate(Student student) {
        return "<h2>Dear " + student.getName() + ",</h2>"
                + "<p>We are pleased to invite you to the <b>Cultural Event</b> at XYZ College!</p>"
                + "<p>Prepare for a day full of art, music, and dance performances that celebrate creativity and talent.</p>"
                + "<p>Event Date: <b>May 5, 2025</b></p>"
                + "<p>Venue: <b>XYZ College Auditorium</b></p>"
                + "<br><hr><p>Best regards, <br> XYZ College Cultural Event Team</p>";
    }

    private String buildScientificTechnicalFestTemplate(Student student) {
        return "<h2>Dear " + student.getName() + ",</h2>"
                + "<p>Get ready for the <b>Scientific and Technical Fest</b>!</p>"
                + "<p>This fest will feature cutting-edge technology and innovation. We are thrilled to have you join us!</p>"
                + "<p>Event Date: <b>June 12, 2025</b></p>"
                + "<p>Venue: <b>XYZ College Auditorium</b></p>"
                + "<br><hr><p>Best regards, <br> XYZ College Science & Tech Fest Team</p>";
    }

    private String buildHackathonTemplate(Student student) {
        return "<h2>Dear " + student.getName() + ",</h2>"
                + "<p>Welcome to the <b>Hackathon at XYZ College</b>!</p>"
                + "<p>We’re excited to have you as part of this exciting event where you’ll be able to collaborate and innovate with other bright minds.</p>"
                + "<p>Event Date: <b>July 20, 2025</b></p>"
                + "<p>Venue: <b>XYZ College Innovation Hub</b></p>"
                + "<br><hr><p>Best regards, <br> XYZ College Hackathon Team</p>";
    }

    public ResponseEntity<String> sendStudentResultsEmail(
            List<String> studentEmails,
            String subject,
            String month,
            List<String> carbonCopy,
            List<String> blindCarbonCopy) throws MessagingException, IOException {

        // Loop through each email to process and send results
        for (String email : studentEmails) {
            // Fetch student by email
            Optional<Student> student = studentRepository.findByEmail(email);

            if (student.isPresent()) {
                // Get the student ID from the student object
                Long studentId = student.get().getId();

                // Generate the PDF for the student's result based on the studentId and month
                byte[] pdfBytes = pdfGenerationService.generateStudentResultPDF(studentId, month);

                // Send the email with the PDF attached
                emailService.sendEmailWithAttachment(
                        email,
                        subject,
                        "Dear Student, please find attached your result for " + month + ".",
                        carbonCopy,
                        blindCarbonCopy,
                        pdfBytes
                );
            } else {
                // If student is not found, throw an exception or log it
                throw new RuntimeException("Student not found with email: " + email);
            }
        }

        // Return response after processing all emails
        return ResponseEntity.ok("Results sent successfully.");
    }
}
