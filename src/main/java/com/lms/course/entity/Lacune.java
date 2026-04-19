package com.lms.course.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lacunes")
public class Lacune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    private Long moduleId;

    private String competence; // ex: "Algorithmique", "SQL"

    private String niveau; // FAIBLE, MOYEN

    private LocalDate detecteLe;

    @PrePersist
    protected void onCreate() {
        this.detecteLe = LocalDate.now();
    }
}
