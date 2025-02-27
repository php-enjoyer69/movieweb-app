package movieweb.movieweb.services;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.reports.ReviewReportDto;
import movieweb.movieweb.entities.Review;
import movieweb.movieweb.entities.ReviewReport;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.mappers.ReviewReportMapper;
import movieweb.movieweb.repositories.ReviewReportRepository;
import movieweb.movieweb.repositories.ReviewRepository;
import movieweb.movieweb.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewReportService {

    private final ReviewReportRepository reviewReportRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewReportMapper reviewReportMapper;

    @Transactional
    public ReviewReportDto reportReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<ReviewReport> existingReport = reviewReportRepository.findByReviewAndUser(review, user);
        if (existingReport.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User has already reported this review");
        }

        ReviewReport report = new ReviewReport();
        report.setReview(review);
        report.setUser(user);
        reviewReportRepository.save(report);

        return reviewReportMapper.toDto(report);
    }

    public List<ReviewReportDto> getAllReports() {
        return reviewReportMapper.toDtoList(reviewReportRepository.findAll());
    }

    public boolean doesReportExist(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<ReviewReport> report = reviewReportRepository.findByReviewAndUser(review, user);
        return report.isPresent();
    }

    public void deleteReport(Long reportId) {
        ReviewReport report = reviewReportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        reviewReportRepository.delete(report);
    }
}
