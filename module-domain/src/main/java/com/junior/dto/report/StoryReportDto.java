package com.junior.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class StoryReportDto extends ReportDto {
    private String title;
    private Long storyId;
}
