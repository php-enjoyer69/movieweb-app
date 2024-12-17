package movieweb.movieweb.loaders;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.movies.NewMovieDto;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.MovieGenre;
import movieweb.movieweb.mappers.MovieMapper;
import movieweb.movieweb.repositories.MovieGenreRepository;
import movieweb.movieweb.repositories.MovieRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class PopulateMovieDataLoader implements ApplicationRunner
{
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;
  private final MovieGenreRepository movieGenreRepository;

  public void run(ApplicationArguments args)
  {
    populateMovieGenres();
    populateMovies();
  }

  private void populateMovies()
  {
    MovieGenre action = movieGenreRepository.getByName("Action");
    MovieGenre drama = movieGenreRepository.getByName("Drama");
    MovieGenre horror = movieGenreRepository.getByName("Horror");

    for (int i = 0; i < 100; i++)
    {
      Set<MovieGenre> movieGenres = new HashSet<>();

      if (i % 2 == 0) {
        movieGenres.add(action);
      }
      else {
        movieGenres.add(drama);
      }

      if (i % 3 == 0) {
        movieGenres.add(horror);
      }

      createMovie(NewMovieDto.builder()
                      .title("Movie " + (i + 1))
                      .description("An interesting production")
                      .genres(movieGenres)
                      .year(Integer.valueOf("2010"))
                      .rating(Double.valueOf("0"))
                      .img("movie1.jpg")
                      .build(),
              movieGenres
      );
    }
  }

  private void createMovie(NewMovieDto newMovieDto, Set<MovieGenre> movieGenres)
  {
    Optional<Movie> existingMovie = movieRepository.findByTitle(newMovieDto.getTitle());

    if (existingMovie.isEmpty())
    {
      Movie movie = movieMapper.newMovieDtoToMovie(newMovieDto);
      movie.setGenres(movieGenres);

      movieRepository.save(movie);
    }
  }

  private void populateMovieGenres()
  {
    createMovieGenre("Action");
    createMovieGenre("Drama");
    createMovieGenre("Horror");
    createMovieGenre("Comedy");
    createMovieGenre("Romantic");
    createMovieGenre("Sci-Fi");
    createMovieGenre("Thriller");
    createMovieGenre("Fantasy");
  }

  private void createMovieGenre(String name)
  {
    Optional<MovieGenre> existingMovieGenre = movieGenreRepository.findByName(name);

    if (existingMovieGenre.isEmpty())
    {
      movieGenreRepository.save(MovieGenre.builder().name(name).build());
    }
  }
}