package movieweb.movieweb.loaders;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.movies.NewMovieDto;
import movieweb.movieweb.entities.Movie;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import movieweb.movieweb.mappers.MovieMapper;
import movieweb.movieweb.repositories.MovieRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PopulateMovieDataLoader implements ApplicationRunner
{
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public void run(ApplicationArguments args)
  {
    populateMovies();
  }

  private void populateMovies()
  {
    for (int i = 0; i < 100; i++)
    {
      createMovie(NewMovieDto.builder()
          .title("Movie " + (i + 1))
          .description("My son loves it!")
          .year(Integer.valueOf("2010"))
          .rating(Double.valueOf("0"))
          .img("movie1.jpg")
          .build());
    }
  }

  private void createMovie(NewMovieDto newMovieDto)
  {
    Optional<Movie> existingMovie = movieRepository.findByTitle(newMovieDto.getTitle());

    if (!existingMovie.isPresent())
    {
      Movie movie = movieMapper.newMovieDtoToMovie(newMovieDto);
      movieRepository.save(movie);
    }
  }
}