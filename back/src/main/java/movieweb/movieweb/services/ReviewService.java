package movieweb.movieweb.services;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.reviews.ReviewDto;
import movieweb.movieweb.dtos.reviews.NewReviewDto;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.Review;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.mappers.ReviewMapper;
import movieweb.movieweb.repositories.MovieRepository;
import movieweb.movieweb.repositories.ReviewRepository;
import movieweb.movieweb.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public ReviewDto addReview(Long movieId, Long userId, NewReviewDto newReviewDto) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = new Review();
        review.setMovie(movie);
        review.setUser(user);
        review.setRating(newReviewDto.getRating());
        review.setContent(newReviewDto.getContent());

        reviewRepository.save(review);

        double newAverageRating = reviewRepository
                .findByMovieId(movieId).stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        movie.setAverageRating(newAverageRating);
        movieRepository.save(movie);

        return reviewMapper.toReviewDto(review);
    }

    public List<ReviewDto> getReviewsByMovie(Long movieId) {
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        return reviews.stream()
                .map(review -> reviewMapper.toReviewDto(review))
                .collect(Collectors.toList());
    }
}
