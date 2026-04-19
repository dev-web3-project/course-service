package com.lms.course.dto;

import lombok.Data;

@Data
public class MaterialRequestDto {
    private String title;
    private String description;
    private String type;
    private String fileUrl;
    private Long moduleId;
}
