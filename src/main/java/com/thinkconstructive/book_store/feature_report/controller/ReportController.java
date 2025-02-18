package com.thinkconstructive.book_store.feature_report.controller;

import com.thinkconstructive.book_store.feature_report.models.ReportDto;
import com.thinkconstructive.book_store.feature_report.models.ReportRequestDto;
import com.thinkconstructive.book_store.feature_report.utils.ReportMapper;
import com.thinkconstructive.book_store.feature_report.repo.ReportRepository;
import com.thinkconstructive.book_store.feature_report.service.ReportProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/kirana/transactions")
public class ReportController {

    private final ReportRepository reportRepository;
    private final ReportProducerService reportProducerService;

    /**
     * Constructs the ReportController
     *
     * @param reportRepository repository to interact with the reports
     * @param reportProducerService service for sending report generation requests
     */
    public ReportController(ReportRepository reportRepository, ReportProducerService reportProducerService) {
        this.reportRepository = reportRepository;
        this.reportProducerService = reportProducerService;
    }

    /**
     * Retrieves the reports for the past week, month, and year
     *
     *
     * @return ResponseEntity containing the report or a message indicating that reports are being generated
     */
    @GetMapping("/getreports")
    public ResponseEntity<?> getReports() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate currentDate = LocalDate.now();
        String currentDateStr = currentDate.format(formatter);
        String weeklyStart = currentDate.minusDays(7).format(formatter);
        String monthlyStart = currentDate.minusMonths(1).format(formatter);
        String yearlyStart = currentDate.minusYears(1).format(formatter);


        boolean weeklyExists = reportRepository.findAll().stream()
                .anyMatch(r -> r.startDate().equals(weeklyStart) && r.endDate().equals(currentDateStr));
        boolean monthlyExists = reportRepository.findAll().stream()
                .anyMatch(r -> r.startDate().equals(monthlyStart) && r.endDate().equals(currentDateStr));
        boolean yearlyExists = reportRepository.findAll().stream()
                .anyMatch(r -> r.startDate().equals(yearlyStart) && r.endDate().equals(currentDateStr));


        if (!weeklyExists || !monthlyExists || !yearlyExists) {
            reportProducerService.sendReportRequest(new ReportRequestDto("weekly", weeklyStart, currentDateStr));
            reportProducerService.sendReportRequest(new ReportRequestDto("monthly", monthlyStart, currentDateStr));
            reportProducerService.sendReportRequest(new ReportRequestDto("yearly", yearlyStart, currentDateStr));
            return ResponseEntity.ok("Generating report, please try again later.");
        }


        List<ReportDto> reportDtos = reportRepository.findAll().stream()
                .filter(r -> (r.startDate().equals(weeklyStart) ||
                        r.startDate().equals(monthlyStart) ||
                        r.startDate().equals(yearlyStart))
                        && r.endDate().equals(currentDateStr))
                .map(ReportMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reportDtos);
    }
}
