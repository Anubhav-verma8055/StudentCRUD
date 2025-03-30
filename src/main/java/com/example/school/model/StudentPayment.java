package com.example.school.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentId;
    private String paymentId;
    private String orderId;
    private double amount;
    private String status;
    private LocalDateTime timestamp;
}