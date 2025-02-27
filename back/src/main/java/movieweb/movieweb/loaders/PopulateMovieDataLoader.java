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

  private final Random rand = new Random();

  public void run(ApplicationArguments args)
  {
    populateMovieGenres();
    populateMovies();
  }

  private void populateMovies()
  {
    List<MovieGenre> allGenres = movieGenreRepository.findAll();

    for (int i = 0; i < 10; i++)
    {
      int numberOfGenres = rand.nextInt(3) + 2;
      Set<MovieGenre> movieGenres = new HashSet<>();

      while (movieGenres.size() < numberOfGenres) {
        movieGenres.add(getRandomMovieGenre(allGenres));
      }

      int randomYear = rand.nextInt(105) + 1920;

      String imageName = "movie" + (rand.nextInt(21) + 1) + ".jpg";

      createMovie(NewMovieDto.builder()
                      .title("Movie " + (i + 1))
                      .description("Two men awaken to find themselves on the opposite sides of a dead body, each with specific instructions to kill the other or face consequences. These two are the latest victims of the Jigsaw Killer.")
                      .genres(movieGenres)
                      .year(randomYear)
                      .averageRating(0.0)
                      .ratingCount(Integer.valueOf(0))
                      .img(imageName)
                      .build(),
              movieGenres
      );
    }
  }

  private void createMovie(NewMovieDto newMovieDto, Set<MovieGenre> movieGenres)
  {
    movieRepository.findByTitle(newMovieDto.getTitle())
            .ifPresentOrElse(existingMovie -> {
            }, () -> {
              Movie movie = movieMapper.newMovieDtoToMovie(newMovieDto);
              movie.setGenres(movieGenres);
              movieRepository.save(movie);
            });
  }

  private void populateMovieGenres()
  {
    createMovieGenre("Action");
    createMovieGenre("Drama");
    createMovieGenre("Horror");
    createMovieGenre("Comedy");
    createMovieGenre("Romantic");
    createMovieGenre("Animation");
    createMovieGenre("Thriller");
    createMovieGenre("Fantasy");
    createMovieGenre("Sci-Fi");
    createMovieGenre("Adventure");
    createMovieGenre("Crime");
    createMovieGenre("Mystery");
    createMovieGenre("Documentary");
    createMovieGenre("Musical");
    createMovieGenre("Family");
  }

  private void createMovieGenre(String name)
  {
    movieGenreRepository.findByName(name)
            .ifPresentOrElse(existingMovieGenre -> {
            }, () -> {
              movieGenreRepository.save(MovieGenre.builder().name(name).build());
            });
  }

  private MovieGenre getRandomMovieGenre(List<MovieGenre> allGenres)
  {
    return allGenres.get(rand.nextInt(allGenres.size()));
  }
}
