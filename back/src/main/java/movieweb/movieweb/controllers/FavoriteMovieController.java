package movieweb.movieweb.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.services.FavoriteMovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/movie/favorites")
public class FavoriteMovieController {
    private final FavoriteMovieService favoriteMovieService;

    @GetMapping("/{userId}/isFavorite/{movieId}")
    public ResponseEntity<Boolean> isFavorite(@PathVariable Long userId, @PathVariable Long movieId) {
        return ResponseEntity.ok(favoriteMovieService.isFavorite(userId, movieId));
    }

    @PostMapping("/{movieId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long movieId, @RequestParam Long userId) {
        favoriteMovieService.addToFavorites(userId, movieId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long movieId, @RequestParam Long userId) {
        favoriteMovieService.removeFromFavorites(userId, movieId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Set<Movie>> getFavorites(@PathVariable Long userId) {
        Set<Movie> favoriteMovies = favoriteMovieService.getUserFavorites(userId);
        return ResponseEntity.ok(favoriteMovies);
    }
}
