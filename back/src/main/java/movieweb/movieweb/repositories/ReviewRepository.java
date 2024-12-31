package movieweb.movieweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import movieweb.movieweb.entities.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByMovieId(Long movieId);

    List<Review> findByUserId(Long userId);
}
