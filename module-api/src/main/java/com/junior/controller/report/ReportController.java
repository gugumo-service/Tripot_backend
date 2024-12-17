package com.junior.controller.report;

import com.junior.dto.report.CreateReportDto;
import com.junior.exception.StatusCode;
import com.junior.response.CommonResponse;
import com.junior.security.UserPrincipal;
import com.junior.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/api/v1/reports")
    public ResponseEntity<CommonResponse<Object>> saveReport(@AuthenticationPrincipal UserPrincipal principal,
                                                            @RequestBody CreateReportDto createReportDto) {

        reportService.save(createReportDto, principal);

        return ResponseEntity.status(StatusCode.REPORT_CREATE_SUCCESS.getHttpCode()).body(CommonResponse.success(StatusCode.REPORT_CREATE_SUCCESS, null));

    }

}
