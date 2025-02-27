package movieweb.movieweb.controllers;

import movieweb.movieweb.services.WatchlistMovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie/watchlist")
public class WatchlistMovieController {
    private final WatchlistMovieService watchlistMovieService;

    public WatchlistMovieController(WatchlistMovieService watchlistMovieService) {
        this.watchlistMovieService = watchlistMovieService;
    }

    @PostMapping("/{movieId}")
    public ResponseEntity<?> addToWatchlist(@RequestParam Long userId, @PathVariable Long movieId) {
        watchlistMovieService.addMovieToWatchlist(userId, movieId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> removeFromWatchlist(@RequestParam Long userId, @PathVariable Long movieId) {
        try {
            watchlistMovieService.removeMovieFromWatchlist(userId, movieId);
        } catch (Exception e) {
            System.out.println("Error during watchlist update: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal server error");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/isInWatchlist/{movieId}")
    public ResponseEntity<Boolean> isInWatchlist(@PathVariable Long userId, @PathVariable Long movieId) {
        boolean exists = watchlistMovieService.isMovieInWatchlist(userId, movieId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getWatchlist(@PathVariable Long userId) {
        return ResponseEntity.ok(watchlistMovieService.getWatchlistByUser(userId));
    }
}
