package com.lms.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmissionDto {
    private Long quizId;
    private String studentId;
    private List<StudentAnswerDto> answers;
    private IntegrityReportDto integrityReport;
}
