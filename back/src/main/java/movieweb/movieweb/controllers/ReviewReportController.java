package movieweb.movieweb.controllers;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.reports.ReviewReportDto;
import movieweb.movieweb.services.ReviewReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReviewReportController {

    private final ReviewReportService reviewReportService;

    @PostMapping("/{reviewId}/user/{userId}")
    public ResponseEntity<ReviewReportDto> reportReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(reviewReportService.reportReview(reviewId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ReviewReportDto>> getAllReports() {
        return ResponseEntity.ok(reviewReportService.getAllReports());
    }

    // Change this method to return a boolean indicating if the report exists
    @GetMapping("/{reviewId}/user/{userId}/exists")
    public ResponseEntity<Boolean> doesReportExist(@PathVariable Long reviewId, @PathVariable Long userId) {
        boolean reportExists = reviewReportService.doesReportExist(reviewId, userId);
        return ResponseEntity.ok(reportExists);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        reviewReportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }
}
