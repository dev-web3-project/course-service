package com.lms.course.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegrityReportDto {
    private Integer mouseExitCount;
    private Integer tabSwitchCount;
    private Boolean autoSubmitted;
    private List<String> suspiciousVideoSegments;
    private String notes;
}
