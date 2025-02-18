package com.thinkconstructive.book_store.feature_report.models;

public record ReportDto(

        String reportId,


        String startDate,

        String endDate,

        Double amountCredited,

        Double amountDebited,

        Double netflow
) {}
