package com.lms.course.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long moduleId;

    private LocalDate date;

    private LocalDateTime heureDebut;

    private int duree; // en minutes

    @OneToMany
    @JoinColumn(name = "evaluation_id")
    private List<Quiz> quizzes;
}
