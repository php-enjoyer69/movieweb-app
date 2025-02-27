package movieweb.movieweb.services;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.persons.PersonDto;
import movieweb.movieweb.dtos.reviews.PatchReviewDto;
import movieweb.movieweb.dtos.reviews.ReviewDto;
import movieweb.movieweb.dtos.reviews.NewReviewDto;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.entities.Review;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.mappers.ReviewMapper;
import movieweb.movieweb.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ReviewReportRepository reviewReportRepository;
    private final ReviewMapper reviewMapper;
    private final EntityManager entityManager;
    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional
    public ReviewDto addReview(Long movieId, Long userId, NewReviewDto newReviewDto) {
        Movie movie = findMovieById(movieId);
        User user = findUserById(userId);

        reviewRepository.findByMovieIdAndUserId(movieId, userId).ifPresent(existingReview -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User has already reviewed this movie");
        });

        Review review = new Review();
        review.setMovie(movie);
        review.setUser(user);
        review.setRating(newReviewDto.getRating());
        review.setContent(newReviewDto.getContent());
        review.setWantsToWatchAgain(newReviewDto.getWantsToWatchAgain());

        reviewRepository.save(review);

        // Update ratingCount and averageRating
        movie.setRatingCount((movie.getRatingCount() != null ? movie.getRatingCount() : 0) + 1);
        updateAverageRating(movieId);  // Recalculate average rating

        movieRepository.save(movie);  // Save the movie with updated ratingCount

        return reviewMapper.toReviewDto(review);
    }

    public Page<ReviewDto> findAll(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAll(pageable);
        return reviews.map(reviewMapper::toReviewDto);
    }

    public List<ReviewDto> getReviewsByMovie(Long movieId) {
        return reviewRepository.findByMovieId(movieId).stream()
                .map(reviewMapper::toReviewDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDto updateReview(Long movieId, Long reviewId, PatchReviewDto patchReviewDto) {
        if (movieId == null || reviewId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid movieId or reviewId");
        }

        Review review = reviewRepository.findByIdAndMovieId(reviewId, movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        reviewMapper.update(review, patchReviewDto);

        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
        updateAverageRating(movieId);

        return reviewMapper.toReviewDto(review);
    }


    public boolean hasUserReviewedMovie(Long movieId, Long userId) {
        return reviewRepository.findByMovieIdAndUserId(movieId, userId).isPresent();
    }

    public ReviewDto getReviewByUserAndMovie(Long movieId, Long userId) {
        Review review = reviewRepository.findByMovieIdAndUserId(movieId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        return reviewMapper.toReviewDto(review);
    }

    @Transactional
    public void deleteReview(Long movieId, Long reviewId) {
        Review review = reviewRepository.findByIdAndMovieId(reviewId, movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        reviewLikeRepository.deleteByReviewId(reviewId);
        reviewReportRepository.deleteByReviewId(reviewId);

        reviewRepository.delete(review);

        Movie movie = findMovieById(movieId);
        movie.setRatingCount(movie.getRatingCount() - 1);

        updateAverageRating(movieId);

        movieRepository.save(movie);
    }


    private Movie findMovieById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void updateAverageRating(Long movieId) {
        double newAverageRating = reviewRepository.findByMovieId(movieId).stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        Movie movie = findMovieById(movieId);
        movie.setAverageRating(newAverageRating);

        movieRepository.save(movie);
    }

    public List<ReviewDto> getReviewsByUser(Long userId) {
        List<Review> reviews = reviewRepository.findByUserId(userId);
        return reviews.stream()
                .map(reviewMapper::toReviewDto)
                .collect(Collectors.toList());
    }

}
