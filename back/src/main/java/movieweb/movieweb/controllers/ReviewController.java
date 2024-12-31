package movieweb.movieweb.controllers;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.reviews.ReviewDto;
import movieweb.movieweb.dtos.reviews.NewReviewDto;
import movieweb.movieweb.services.ReviewService;
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
}
