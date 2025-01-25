package com.junior.dto.report;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class StoryReportDto extends ReportDto{
    private String title;
    private Long storyId;
}
