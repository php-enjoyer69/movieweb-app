package movieweb.movieweb.controllers;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.reviews.PatchReviewDto;
import movieweb.movieweb.dtos.reviews.ReviewDto;
import movieweb.movieweb.dtos.reviews.NewReviewDto;
import movieweb.movieweb.services.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/movie/{movieId}/review/{userId}")
    public ResponseEntity<ReviewDto> addReview(
            @PathVariable Long movieId,
            @PathVariable Long userId,
            @RequestBody NewReviewDto newReviewDto
    ) {
        ReviewDto reviewDto = reviewService.addReview(movieId, userId, newReviewDto);
        return ResponseEntity.ok(reviewDto);
    }

    @GetMapping("/movie/{movieId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviewsByMovie(@PathVariable Long movieId) {
        List<ReviewDto> reviewDto = reviewService.getReviewsByMovie(movieId);
        return ResponseEntity.ok(reviewDto);
    }

    @GetMapping("/movie/{movieId}/review/{userId}")
    public ResponseEntity<ReviewDto> getReviewByUserAndMovie(
            @PathVariable Long movieId,
            @PathVariable Long userId
    ) {
        ReviewDto reviewDto = reviewService.getReviewByUserAndMovie(movieId, userId);
        if (reviewDto == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(reviewDto);
    }

    @PatchMapping("/movie/{movieId}/review/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable Long movieId,
            @PathVariable Long reviewId,
            @RequestBody PatchReviewDto patchReviewDto
    ) {
        ReviewDto updatedReview = reviewService.updateReview(movieId, reviewId, patchReviewDto);
        return ResponseEntity.ok(updatedReview);
    }


    @GetMapping("/movie/{movieId}/review/{userId}/exists")
    public ResponseEntity<Boolean> hasUserReviewedMovie(
            @PathVariable Long movieId,
            @PathVariable Long userId
    ) {
        boolean hasReviewed = reviewService.hasUserReviewedMovie(movieId, userId);
        return ResponseEntity.ok(hasReviewed);
    }

    @DeleteMapping("/movie/{movieId}/review/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long movieId,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(movieId, reviewId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviewsByUser(@PathVariable Long userId) {
        List<ReviewDto> reviewDtos = reviewService.getReviewsByUser(userId);
        return ResponseEntity.ok(reviewDtos);
    }

    @GetMapping("/reviews")
    public ResponseEntity<Page<ReviewDto>> getAllReviews(Pageable pageable) {
        Page<ReviewDto> reviews = reviewService.findAll(pageable);
        return ResponseEntity.ok(reviews);
    }


}
