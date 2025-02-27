package movieweb.movieweb.repositories;

import movieweb.movieweb.entities.FavoriteMovie;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface FavoriteMovieRepository extends JpaRepository<FavoriteMovie, Long> {
    Optional<FavoriteMovie> findByUserIdAndMovieId(Long userId, Long movieId);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    void deleteByUserIdAndMovieId(Long userId, Long movieId);

    Set<FavoriteMovie> findByUserId(Long userId);
}
