package movieweb.movieweb.repositories;

import movieweb.movieweb.entities.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    ReviewLike findByReviewIdAndUserId(Long reviewId, Long userId);
    List<ReviewLike> findByReviewId(Long reviewId);
    void deleteByReviewId(Long reviewId);
}
