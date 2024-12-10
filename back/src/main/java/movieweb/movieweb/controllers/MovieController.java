package movieweb.movieweb.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import movieweb.movieweb.entities.Image;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.services.ImageService;
import movieweb.movieweb.services.MovieService;
import movieweb.movieweb.dtos.movies.MovieDto;
import movieweb.movieweb.dtos.movies.NewMovieDto;
import movieweb.movieweb.dtos.movies.PatchMovieDto;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Validated
public class MovieController
{
  private final MovieService movieService;
  private final ImageService imageService;

  @GetMapping("/movie/{id}")
  public ResponseEntity<MovieDto> getOne(@PathVariable Long id)
  {
    MovieDto movieDto = movieService.findById(id);

    return ResponseEntity.ok(movieDto);
  }

  @GetMapping("/movies")
  public ResponseEntity<Page<Movie>> getAll(Pageable pageable, @RequestParam(required = false) String search)
  {
    Page<Movie> movies = (search != null)
        ? movieService.findAll(pageable, search)
        : movieService.findAll(pageable);

    return ResponseEntity.ok(movies);
  }

  @PostMapping("/movie")
  public ResponseEntity<MovieDto> add(
      @RequestPart("movie") @Valid NewMovieDto newMovieDto,
      @RequestPart("image") MultipartFile image
  ) throws IOException
  {
    Image savedImage = imageService.upload(image);

    newMovieDto.setImg(savedImage.getName());
    MovieDto movieDto = movieService.save(newMovieDto);

    return ResponseEntity.ok(movieDto);
  }

  @PatchMapping("/movie/{id}")
  public ResponseEntity<MovieDto> update(
      @RequestPart(value="movie", required=false) @Valid PatchMovieDto patchMovieDto,
      @RequestPart(value="image", required=false) MultipartFile patchImage,
      @PathVariable Long id
  ) throws IOException
  {
    if (patchImage == null)
    {
      MovieDto movieDto = movieService.update(patchMovieDto, id);
      return ResponseEntity.ok(movieDto);
    }

    MovieDto movieDto = movieService.findById(id);
    Image updatedImage = imageService.update(movieDto.getImg(), patchImage);

    MovieDto patchedMovieDto = movieService.update(patchMovieDto, updatedImage.getName(), id);

    return ResponseEntity.ok(patchedMovieDto);
  }

  @DeleteMapping("/movie/{id}")
  public void delete(@PathVariable Long id)
  {
    Movie movie = movieService.delete(id);
    imageService.delete(movie.getImg());
  }
}
