package movieweb.movieweb.services;

import com.google.common.base.Joiner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.movies.MovieDto;
import movieweb.movieweb.dtos.movies.NewMovieDto;
import movieweb.movieweb.dtos.movies.PatchMovieDto;
import movieweb.movieweb.dtos.roles.RoleAssignmentRequest;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.entities.PersonMovieRole;
import movieweb.movieweb.enums.Role;
import movieweb.movieweb.repositories.PersonMovieRoleRepository;
import movieweb.movieweb.repositories.PersonRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import movieweb.movieweb.enums.SearchOperation;
import movieweb.movieweb.exceptions.AppException;
import movieweb.movieweb.mappers.MovieMapper;
import movieweb.movieweb.repositories.MovieRepository;
import movieweb.movieweb.specifications.MovieSpecificationsBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class MovieService
{
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;
  private final PersonRepository personRepository;
  private final PersonMovieRoleService personMovieRoleService;
  private final PersonMovieRoleRepository personMovieRoleRepository;
  @PersistenceContext
  EntityManager entityManager;

  public MovieDto findById(Long id)
  {
    Movie movie = movieRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown movie", HttpStatus.NOT_FOUND));

    return movieMapper.toMovieDto(movie);
  }

  public Page<Movie> findAll(Pageable pageable)
  {
    Page<Movie> movies = movieRepository.findAll(pageable);

    return movies;
  }

  public Page<Movie> findAll(Pageable pageable, String search)
  {
    MovieSpecificationsBuilder builder = new MovieSpecificationsBuilder();

    String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
    Pattern pattern = Pattern.compile(
      "(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),"
    );

    if (search != null)
    {
      Matcher matcher = pattern.matcher(search + ",");

      while (matcher.find())
      {
        builder.with(
          matcher.group(1),
          matcher.group(2),
          matcher.group(4),
          matcher.group(3),
          matcher.group(5)
        );
      }
    }

    Specification<Movie> movieSpecification = builder.build();
    Page<Movie> movies = movieRepository.findAll(movieSpecification, pageable);

    return movies;
  }

  public MovieDto save(NewMovieDto newMovieDto) throws IOException
  {
    Movie movie = movieMapper.newMovieDtoToMovie(newMovieDto);
    Movie savedMovie = movieRepository.save(movie);

    return movieMapper.toMovieDto(savedMovie);
  }

  public MovieDto update(PatchMovieDto patchMovieDto, Long id)
  {
    Movie movie = movieRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown movie", HttpStatus.NOT_FOUND));

    movieMapper.update(movie, patchMovieDto);
    Movie updatedMovie = movieRepository.save(movie);

    return movieMapper.toMovieDto(updatedMovie);
  }

  public MovieDto update(PatchMovieDto patchMovieDto, String img, Long id)
  {
    Movie movie = movieRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown movie", HttpStatus.NOT_FOUND));

    movieMapper.update(movie, patchMovieDto);
    movie.setImg(img);

    Movie updatedMovie = movieRepository.save(movie);

    return movieMapper.toMovieDto(updatedMovie);
  }

  @Transactional
  public Movie delete(Long id)
  {
    Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new AppException("Unknown movie", HttpStatus.NOT_FOUND));

    String deleteJoinTableQuery = "DELETE FROM movie_genre_map WHERE movie_id = ?";
    Query query = entityManager.createNativeQuery(deleteJoinTableQuery);
    query.setParameter(1, id);
    query.executeUpdate();

    try {
      movieRepository.delete(movie);
    }
    catch (DataIntegrityViolationException ex)
    {
      throw new AppException("Cannot delete the movie", HttpStatus.BAD_REQUEST);
    }

    return movie;
  }

  @Transactional
  public PersonMovieRole assignRoleToMovie(Long movieId, Long personId, Role role) {
    Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new AppException("Unknown movie", HttpStatus.NOT_FOUND));

    Person person = personRepository.findById(personId)
            .orElseThrow(() -> new AppException("Unknown person", HttpStatus.NOT_FOUND));

    PersonMovieRole personMovieRole = new PersonMovieRole();
    personMovieRole.setMovie(movie);
    personMovieRole.setPerson(person);
    personMovieRole.setRole(role);

    return personMovieRoleService.addPersonMovieRole(personMovieRole);
  }

  @Transactional
  public void assignRolesToMovie(Long movieId, List<RoleAssignmentRequest> roles) {
    Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new AppException("Unknown movie", HttpStatus.NOT_FOUND));

    for (RoleAssignmentRequest request : roles) {
      Person person = personRepository.findById(request.getPersonId())
              .orElseThrow(() -> new AppException("Unknown person", HttpStatus.NOT_FOUND));

      PersonMovieRole personMovieRole = new PersonMovieRole();
      personMovieRole.setMovie(movie);
      personMovieRole.setPerson(person);
      personMovieRole.setRole(request.getRole());

      personMovieRoleRepository.save(personMovieRole);
    }
  }

}
