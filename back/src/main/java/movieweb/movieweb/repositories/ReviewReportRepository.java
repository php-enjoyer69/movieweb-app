package movieweb.movieweb.repositories;

import movieweb.movieweb.entities.Review;
import movieweb.movieweb.entities.ReviewReport;
import movieweb.movieweb.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {
    Optional<ReviewReport> findByReviewAndUser(Review review, User user);
    List<ReviewReport> findByUser(User user);
    void deleteByReviewId(Long reviewId);
}
