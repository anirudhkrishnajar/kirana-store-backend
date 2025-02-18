package com.thinkconstructive.book_store.feature_report.repo;

import com.thinkconstructive.book_store.feature_report.models.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface ReportRepository extends MongoRepository<Report, String> {


    @Query("{ 'startDate': { $in: [?0, ?1, ?2] }, 'endDate': ?3 }")
    List<Report> findReportsByStartDatesAndEndDate(String weeklyStart, String monthlyStart, String yearlyStart, String currentDateStr);
}
