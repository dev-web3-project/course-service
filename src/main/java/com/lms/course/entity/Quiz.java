package com.lms.course.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private Integer timeLimit; // in minutes - global or per question?
    private Boolean timeLimitPerQuestion; // true if time limit is per question
    private String typeQuiz; // QCM, VRAI_FAUX, MIXTE

    // --- Quiz Configuration ---
    @Enumerated(EnumType.STRING)
    private DocumentMode documentMode; // NO_DOCS, PARTIAL, FULL

    private Boolean cameraSurveillanceEnabled;
    private Boolean randomizeQuestions;
    private Boolean randomizeAnswers;
    private Integer maxAttempts; // 1 to unlimited (null for unlimited)
    private LocalDateTime publishDate;
    private LocalDateTime dueDate;
    private Boolean isDraft;
    private Double passingScore; // percentage

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private CModule module;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuizResult> results;

    public enum DocumentMode {
        NO_DOCS,
        PARTIAL,
        FULL
    }
}
