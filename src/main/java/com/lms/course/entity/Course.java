package com.lms.course.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cId;

    private String title;
    private String description;
    private String lecturer;
    private Long duration; // Duration in year
    private String level;
    private String language;
    private String format; // Can be 'online', 'in-person', or 'hybrid'
    private Long credits;

    private Long departmentId;

    private String status; // DRAFT, PUBLISHED, ARCHIVED
    
    @Column(columnDefinition = "TEXT")
    private String contentBlocs;
    
    private LocalDate datePublication;
    private Double noteAverage;
    private Integer xpRecompense;
    private String version;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private CModule module;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        int randomNum = (int)(Math.random() * 9000) + 1000;
        this.cId = "CO" + randomNum;
        this.createdDate = LocalDateTime.now();
    }

}

