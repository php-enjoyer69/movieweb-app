package movieweb.movieweb.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.entities.WatchlistMovie;
import movieweb.movieweb.repositories.MovieRepository;
import movieweb.movieweb.repositories.UserRepository;
import movieweb.movieweb.repositories.WatchlistMovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchlistMovieService {
    private final WatchlistMovieRepository watchlistMovieRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public WatchlistMovieService(WatchlistMovieRepository watchlistMovieRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.watchlistMovieRepository = watchlistMovieRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public void addMovieToWatchlist(Long userId, Long movieId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Movie> movie = movieRepository.findById(movieId);

        if (user.isPresent() && movie.isPresent() && !watchlistMovieRepository.existsByUserIdAndMovieId(userId, movieId)) {
            WatchlistMovie watchlistMovie = new WatchlistMovie();
            watchlistMovie.setUser(user.get());
            watchlistMovie.setMovie(movie.get());
            watchlistMovieRepository.save(watchlistMovie);
        }
    }

    @Transactional
    public void removeMovieFromWatchlist(Long userId, Long movieId) {
        Optional<WatchlistMovie> watchlist = watchlistMovieRepository.findByUserIdAndMovieId(userId, movieId);
        if (watchlist.isPresent()) {
            watchlistMovieRepository.delete(watchlist.get());
        } else {
            throw new EntityNotFoundException("Movie not found in watchlist");
        }
    }

    public boolean isMovieInWatchlist(Long userId, Long movieId) {
        return watchlistMovieRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    public List<WatchlistMovie> getWatchlistByUser(Long userId) {
        return watchlistMovieRepository.findByUserId(userId);
    }
}
