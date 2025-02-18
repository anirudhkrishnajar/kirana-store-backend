package com.thinkconstructive.book_store.feature_report.models;


public record ReportRequestDto(

        String reportType,

        String startDate,

        String endDate
) {}
