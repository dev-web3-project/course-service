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
@Table(name = "quiz_results")
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;
    
    private String studentId;
    private Double score;
    private LocalDateTime startedDate;
    private LocalDateTime completedDate;
    private Integer attemptNumber;
    private Boolean passed;

    @OneToMany(mappedBy = "quizResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentAnswer> answers;

    @OneToOne(mappedBy = "quizResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private IntegrityReport integrityReport;

    @PrePersist
    protected void onCreate() {
        if (this.startedDate == null) {
            this.startedDate = LocalDateTime.now();
        }
    }
}
