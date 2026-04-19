package com.lms.course.dto;

import com.lms.course.entity.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCreateDto {
    private String title;
    private String description;
    private Integer timeLimit;
    private Boolean timeLimitPerQuestion;
    private Quiz.DocumentMode documentMode;
    private Boolean cameraSurveillanceEnabled;
    private Boolean randomizeQuestions;
    private Boolean randomizeAnswers;
    private Integer maxAttempts;
    private LocalDateTime publishDate;
    private LocalDateTime dueDate;
    private Boolean isDraft;
    private Double passingScore;
    private Long moduleId;
    private List<QuestionCreateDto> questions;
}
