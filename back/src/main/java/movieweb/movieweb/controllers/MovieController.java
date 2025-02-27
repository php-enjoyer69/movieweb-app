package movieweb.movieweb.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.roles.RoleAssignmentRequest;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.enums.Role;
import movieweb.movieweb.exceptions.AppException;
import movieweb.movieweb.repositories.MovieRepository;
import movieweb.movieweb.repositories.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import movieweb.movieweb.entities.Image;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.PersonMovieRole;
import movieweb.movieweb.services.ImageService;
import movieweb.movieweb.services.MovieService;
import movieweb.movieweb.services.PersonMovieRoleService;
import movieweb.movieweb.dtos.movies.MovieDto;
import movieweb.movieweb.dtos.movies.NewMovieDto;
import movieweb.movieweb.dtos.movies.PatchMovieDto;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
public class MovieController {
  private final MovieService movieService;
  private final ImageService imageService;
  private final PersonMovieRoleService personMovieRoleService;
  private final MovieRepository movieRepository;
  private final PersonRepository personRepository;

  @GetMapping("/movie/{id}")
  public ResponseEntity<MovieDto> getOne(@PathVariable Long id) {
    MovieDto movieDto = movieService.findById(id);
    return ResponseEntity.ok(movieDto);
  }

  @GetMapping("/movies")
  public ResponseEntity<Page<Movie>> getAll(Pageable pageable, @RequestParam(required = false) String search) {
    Page<Movie> movies = (search != null)
            ? movieService.findAll(pageable, search)
            : movieService.findAll(pageable);
    return ResponseEntity.ok(movies);
  }

  @PostMapping("/movie")
  public ResponseEntity<MovieDto> add(
          @RequestPart("movie") @Valid NewMovieDto newMovieDto,
          @RequestPart("image") MultipartFile image
  ) throws IOException {
    Image savedImage = imageService.upload(image);
    newMovieDto.setImg(savedImage.getName());
    MovieDto movieDto = movieService.save(newMovieDto);
    return ResponseEntity.ok(movieDto);
  }

  @PatchMapping("/movie/{id}")
  public ResponseEntity<MovieDto> update(
          @RequestPart(value = "movie", required = false) @Valid PatchMovieDto patchMovieDto,
          @RequestPart(value = "image", required = false) MultipartFile patchImage,
          @PathVariable Long id
  ) throws IOException {
    if (patchImage == null) {
      MovieDto movieDto = movieService.update(patchMovieDto, id);
      return ResponseEntity.ok(movieDto);
    }

    MovieDto movieDto = movieService.findById(id);
    Image updatedImage = imageService.update(movieDto.getImg(), patchImage);
    MovieDto patchedMovieDto = movieService.update(patchMovieDto, updatedImage.getName(), id);
    return ResponseEntity.ok(patchedMovieDto);
  }

  @DeleteMapping("/movie/{id}")
  public void delete(@PathVariable Long id) {
    Movie movie = movieService.delete(id);
    imageService.delete(movie.getImg());
  }

  @GetMapping("/movie/{id}/roles")
  public ResponseEntity<List<PersonMovieRole>> getRolesByMovieId(@PathVariable Long id) {
    List<PersonMovieRole> roles = personMovieRoleService.getRolesByMovieId(id);
    if (roles.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(roles);
    }
    return ResponseEntity.ok(roles);
  }

  @PostMapping("/movie/{movieId}/role")
  public ResponseEntity<?> assignRoleToMovie(@PathVariable Long movieId,
                                             @RequestParam Long personId,
                                             @RequestParam Role role) {
    PersonMovieRole savedRole = movieService.assignRoleToMovie(movieId, personId, role);
    return ResponseEntity.ok(savedRole);
  }

  @PostMapping("/movie/{movieId}/roles")
  public ResponseEntity<?> assignRolesToMovie(@PathVariable Long movieId, @RequestBody List<RoleAssignmentRequest> roles) {
    movieService.assignRolesToMovie(movieId, roles);
    return ResponseEntity.ok("Roles assigned successfully");
  }
}