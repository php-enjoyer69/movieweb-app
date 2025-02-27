package movieweb.movieweb.controllers;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.reports.CommentReportDto;
import movieweb.movieweb.services.CommentReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment-reports")
public class CommentReportController {

    private final CommentReportService commentReportService;

    @PostMapping("/{commentId}/user/{userId}")
    public ResponseEntity<CommentReportDto> reportComment(
            @PathVariable Long commentId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(commentReportService.reportComment(commentId, userId));
    }

    @GetMapping
    public ResponseEntity<List<CommentReportDto>> getAllReports() {
        return ResponseEntity.ok(commentReportService.getAllReports());
    }

    @GetMapping("/{commentId}/user/{userId}/exists")
    public ResponseEntity<Boolean> doesReportExist(@PathVariable Long commentId, @PathVariable Long userId) {
        boolean reportExists = commentReportService.doesReportExist(commentId, userId);
        return ResponseEntity.ok(reportExists);
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long reportId) {
        commentReportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }
}
