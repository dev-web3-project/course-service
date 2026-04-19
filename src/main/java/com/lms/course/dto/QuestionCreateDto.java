package com.lms.course.dto;

import com.lms.course.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateDto {
    private String text;
    private String hint;
    private Integer orderIndex;
    private Integer points;
    private Question.QuestionType type;
    private List<QuestionOptionCreateDto> options;
}
