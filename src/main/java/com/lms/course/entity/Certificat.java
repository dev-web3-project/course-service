package com.lms.course.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "certificats")
public class Certificat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long moduleId; // optionnel, peut être pour tout le cours

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false)
    private Long studentId;

    private String urlPdf;

    @Column(unique = true, nullable = false)
    private String codeVerification;

    private LocalDateTime dateEmission;

    @PrePersist
    protected void onCreate() {
        this.dateEmission = LocalDateTime.now();
        if (this.codeVerification == null) {
            this.codeVerification = java.util.UUID.randomUUID().toString();
        }
    }
}
