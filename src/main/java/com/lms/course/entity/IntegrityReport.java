package com.lms.course.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "integrity_reports")
public class IntegrityReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "quiz_result_id", nullable = false)
    @JsonIgnore
    private QuizResult quizResult;

    private Integer integrityScore; // 0-100
    private Integer mouseExitCount;
    private Integer tabSwitchCount;
    private Boolean autoSubmitted;
    private List<String> suspiciousVideoSegments; // URLs or timestamps
    private String notes;
}
