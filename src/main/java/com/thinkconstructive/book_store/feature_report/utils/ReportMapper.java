package com.thinkconstructive.book_store.feature_report.utils;

import com.thinkconstructive.book_store.feature_report.models.ReportDto;
import com.thinkconstructive.book_store.feature_report.models.Report;

public class ReportMapper {

    /**
     * Converts Report entity to ReportDto
     *
     * @param report Report entity to be converted
     * @return ReportDto containing the same data as the Report entity
     */
    public static ReportDto toDto(Report report) {
        return new ReportDto(
                report.reportId(),
                report.startDate(),
                report.endDate(),
                report.amountCredited(),
                report.amountDebited(),
                report.netflow()
        );
    }

    /**
     * Converts ReportDto to Report entity
     *
     * @param reportDto ReportDto to be converted
     * @return Report entity containing the same data from the ReportDto
     */
    public static Report toEntity(ReportDto reportDto) {
        return new Report(
                reportDto.reportId(),
                reportDto.startDate(),
                reportDto.endDate(),
                reportDto.amountCredited(),
                reportDto.amountDebited(),
                reportDto.netflow()
        );
    }
}
