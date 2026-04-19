package com.lms.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumThreadCreateDto {
    private String title;
    private String content;
    private String authorId;
    private String authorName;
    private Long moduleId;
}
