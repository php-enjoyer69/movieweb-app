package movieweb.movieweb.repositories;

import movieweb.movieweb.entities.WatchlistMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistMovieRepository extends JpaRepository<WatchlistMovie, Long> {
    List<WatchlistMovie> findByUserId(Long userId);

    Optional<WatchlistMovie> findByUserIdAndMovieId(Long userId, Long movieId);

    void deleteByUserIdAndMovieId(Long userId, Long movieId);

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
}
