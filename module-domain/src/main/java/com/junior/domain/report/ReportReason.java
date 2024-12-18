package com.junior.domain.report;

import com.junior.exception.ReportException;
import com.junior.exception.StatusCode;

public enum ReportReason {
    SPAMMARKET("스팸홍보"),
    SPAMLOT("도배"),
    ADULT("음란물"),
    ILLEGAL("불법정보 포함"),
    FWORD("욕설/인신공격"),
    PERSONAL("개인정보노출");

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
