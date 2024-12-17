package movieweb.movieweb.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import movieweb.movieweb.dtos.movieGenres.NewMovieGenreDto;
import movieweb.movieweb.dtos.movieGenres.PatchMovieGenreDto;
import movieweb.movieweb.dtos.movieGenres.MovieGenreDto;
import movieweb.movieweb.entities.MovieGenre;
import movieweb.movieweb.services.MovieGenreService;

@RequiredArgsConstructor
@RestController
@Validated
public class MovieGenreController
{
  private final MovieGenreService movieGenreService;

  @GetMapping("/movie-genre/{id}")
  public ResponseEntity<MovieGenreDto> getOne(@PathVariable Long id)
  {
    MovieGenreDto movieGenreDto = movieGenreService.findById(id);
    return ResponseEntity.ok(movieGenreDto);
  }

  @GetMapping("/movie-genres")
  public ResponseEntity<Page<MovieGenre>> getAll(Pageable pageable, @RequestParam(required = false) String search)
  {
    Page<MovieGenre> genres = movieGenreService.findAll(pageable);
    return ResponseEntity.ok(genres);
  }

  @PostMapping("/movie-genre")
  public ResponseEntity<MovieGenreDto> add(@RequestBody @Valid NewMovieGenreDto newMovieGenreDto)
  {
    MovieGenreDto movieGenreDto = movieGenreService.save(newMovieGenreDto);
    return ResponseEntity.ok(movieGenreDto);
  }

  @PatchMapping("/movie-genre/{id}")
  public ResponseEntity<MovieGenreDto> update(
      @RequestBody @Valid PatchMovieGenreDto patchMovieGenreDto,
      @PathVariable Long id
  ) {
    MovieGenreDto updatedMovieGenreDto = movieGenreService.update(patchMovieGenreDto, id);
    return ResponseEntity.ok(updatedMovieGenreDto);
  }

  @DeleteMapping("/movie-genre/{id}")
  public void delete(@PathVariable Long id)
  {
    movieGenreService.delete(id);
  }
}