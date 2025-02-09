package com.junior.repository.report;

import com.junior.domain.report.ReportStatus;
import com.junior.dto.report.ReportQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepositoryCustom {

    Page<ReportQueryDto> findReport(ReportStatus reportStatus, Pageable pageable);
}
