package com.lms.course.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "modules")
public class CModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mId;
    private String title;
    private String description;
    private Long duration; // Duration in hours
    private String level;
    private String language;
    private Long credits;
    private String semester;
    private String lecturer;

    private LocalDate dateDisponible;
    private String videoUrl;

    @Column(columnDefinition = "TEXT")
    private String transcription;

    private Integer ordre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "module", orphanRemoval = true)
    private Set<Course> courses = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        int randomNum = (int)(Math.random() * 9000) + 1000;
        this.mId = "MD" + randomNum;
        this.createdDate = LocalDateTime.now();
    }
}
