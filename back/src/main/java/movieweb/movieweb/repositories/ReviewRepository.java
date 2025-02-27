package movieweb.movieweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import movieweb.movieweb.entities.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findByMovieId(Long movieId);

    List<Review> findByUserId(Long userId);

    Optional<Review> findByMovieIdAndUserId(Long movieId, Long userId);

    Optional<Review> findByIdAndMovieId(Long reviewId, Long movieId);

}