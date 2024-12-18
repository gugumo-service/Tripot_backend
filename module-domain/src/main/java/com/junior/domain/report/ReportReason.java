package com.junior.domain.report;

import com.junior.exception.ReportException;
import com.junior.exception.StatusCode;

public enum ReportReason {
    SAMPLE("샘플");

    private final String name;
    private ReportReason(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static ReportReason nameOf(String name) {
        for (ReportReason reason : ReportReason.values()) {
            if (reason.getName().equals(name)) {
                return reason;
            }
        }
        throw new ReportException(StatusCode.REPORT_NOT_VALID);
    }
}
