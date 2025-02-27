package movieweb.movieweb.services;

import movieweb.movieweb.entities.FavoriteMovie;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.repositories.FavoriteMovieRepository;
import movieweb.movieweb.repositories.MovieRepository;
import movieweb.movieweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FavoriteMovieService {

    private final FavoriteMovieRepository favoriteMovieRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public boolean isFavorite(Long userId, Long movieId) {
        return favoriteMovieRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    public void addToFavorites(Long userId, Long movieId) {
        if (!favoriteMovieRepository.existsByUserIdAndMovieId(userId, movieId)) {
            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new IllegalArgumentException("Movie not found"));

            FavoriteMovie favoriteMovie = new FavoriteMovie();
            favoriteMovie.setUser(user);
            favoriteMovie.setMovie(movie);
            favoriteMovieRepository.save(favoriteMovie);
        }
    }

    public void removeFromFavorites(Long userId, Long movieId) {
        FavoriteMovie favoriteMovie = favoriteMovieRepository.findByUserIdAndMovieId(userId, movieId)
                .orElseThrow(() -> new IllegalArgumentException("Favorite movie not found"));
        favoriteMovieRepository.delete(favoriteMovie);
    }

    public Set<Movie> getUserFavorites(Long userId) {
        Set<FavoriteMovie> favoriteMovies = favoriteMovieRepository.findByUserId(userId);
        return favoriteMovies.stream()
                .map(FavoriteMovie::getMovie)
                .collect(Collectors.toSet());
    }
}
