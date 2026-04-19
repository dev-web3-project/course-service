package com.lms.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumPostCreateDto {
    private String content;
    private String authorId;
    private String authorName;
    private Long threadId;
    private Long parentPostId;
}
