package com.thinkconstructive.book_store.feature_report.models;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "reports")
public record Report(

        String reportId,

        String startDate,


        String endDate,


        Double amountCredited,

        Double amountDebited,


        Double netflow
) {}
