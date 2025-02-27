package movieweb.movieweb.services;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.entities.Review;
import movieweb.movieweb.entities.ReviewLike;
import movieweb.movieweb.enums.LikeType;
import movieweb.movieweb.exceptions.AppException;
import movieweb.movieweb.repositories.ReviewLikeRepository;
import movieweb.movieweb.repositories.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public void likeReview(Long reviewId, Long userId, LikeType likeType) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException("Review not found", HttpStatus.NOT_FOUND));

        ReviewLike existingLike = reviewLikeRepository.findByReviewIdAndUserId(reviewId, userId);

        if (existingLike != null) {
            if (existingLike.getLikeType() == likeType) {
                reviewLikeRepository.delete(existingLike);
                if (likeType == LikeType.LIKE) {
                    review.setLikeCount(review.getLikeCount() - 1);
                } else {
                    review.setDislikeCount(review.getDislikeCount() - 1);
                }
            } else {
                if (likeType == LikeType.LIKE) {
                    review.setLikeCount(review.getLikeCount() + 1);
                    review.setDislikeCount(review.getDislikeCount() - 1);
                } else {
                    review.setLikeCount(review.getLikeCount() - 1);
                    review.setDislikeCount(review.getDislikeCount() + 1);
                }
                existingLike.setLikeType(likeType);
                reviewLikeRepository.save(existingLike);
            }
        } else {
            ReviewLike newLike = ReviewLike.builder()
                    .review(review)
                    .userId(userId)
                    .likeType(likeType)
                    .build();
            reviewLikeRepository.save(newLike);

            if (likeType == LikeType.LIKE) {
                review.setLikeCount(review.getLikeCount() + 1);
            } else {
                review.setDislikeCount(review.getDislikeCount() + 1);
            }
        }

        reviewRepository.save(review);
    }


    public List<ReviewLike> findLikesByReview(Long reviewId) {
        return reviewLikeRepository.findByReviewId(reviewId);
    }

    public LikeType getUserLikeType(Long reviewId, Long userId) {
        ReviewLike existingLike = reviewLikeRepository.findByReviewIdAndUserId(reviewId, userId);

        if (existingLike != null) {
            return existingLike.getLikeType();
        }

        return null;
    }
}
