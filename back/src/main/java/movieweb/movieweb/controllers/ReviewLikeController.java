package movieweb.movieweb.controllers;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.enums.LikeType;
import movieweb.movieweb.services.ReviewLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import movieweb.movieweb.entities.ReviewLike;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Void> likeReview(
            @PathVariable Long reviewId,
            @RequestParam Long userId,
            @RequestParam LikeType likeType
    ) {
        reviewLikeService.likeReview(reviewId, userId, likeType);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{reviewId}/likes")
    public ResponseEntity<List<ReviewLike>> getLikes(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewLikeService.findLikesByReview(reviewId));
    }

    @GetMapping("/{reviewId}/like/{userId}/exists")
    public ResponseEntity<LikeType> checkIfUserLikedReview(
            @PathVariable Long reviewId,
            @PathVariable Long userId
    ) {
        LikeType likeType = reviewLikeService.getUserLikeType(reviewId, userId);

        if (likeType == null) {
            return ResponseEntity.ok(LikeType.NONE);
        }
        return ResponseEntity.ok(likeType);
    }
}
