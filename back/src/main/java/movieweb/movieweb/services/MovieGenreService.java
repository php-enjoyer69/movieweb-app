package movieweb.movieweb.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import movieweb.movieweb.dtos.movieGenres.NewMovieGenreDto;
import movieweb.movieweb.dtos.movieGenres.PatchMovieGenreDto;
import movieweb.movieweb.dtos.movieGenres.MovieGenreDto;
import movieweb.movieweb.entities.MovieGenre;
import movieweb.movieweb.exceptions.AppException;
import movieweb.movieweb.mappers.MovieGenreMapper;
import movieweb.movieweb.repositories.MovieGenreRepository;

@RequiredArgsConstructor
@Service
public class MovieGenreService
{
  private final MovieGenreRepository movieGenreRepository;
  private final MovieGenreMapper movieGenreMapper;
  @PersistenceContext
  EntityManager entityManager;

  public MovieGenreDto findById(Long id)
  {
    MovieGenre movieGenre = movieGenreRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown movie genre", HttpStatus.NOT_FOUND));

    return movieGenreMapper.toMovieGenreDto(movieGenre);
  }

  public Page<MovieGenre> findAll(Pageable pageable)
  {
    Page<MovieGenre> genres = movieGenreRepository.findAll(pageable);
    return genres;
  }

  public MovieGenreDto save(NewMovieGenreDto newMovieGenreDto)
  {
    MovieGenre movieGenre = movieGenreMapper.toMovieGenre(newMovieGenreDto);
    MovieGenre savedGenre = movieGenreRepository.save(movieGenre);

    return movieGenreMapper.toMovieGenreDto(savedGenre);
  }

  public MovieGenreDto update(PatchMovieGenreDto patchMovieGenreDto, Long id)
  {
    MovieGenre movieGenre = movieGenreRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown movie genre", HttpStatus.NOT_FOUND));

    movieGenreMapper.update(movieGenre, patchMovieGenreDto);
    MovieGenre updatedGenre = movieGenreRepository.save(movieGenre);

    return movieGenreMapper.toMovieGenreDto(updatedGenre);
  }

  @Transactional
  public void delete(Long id)
  {
    MovieGenre movieGenre = movieGenreRepository.findById(id)
        .orElseThrow(() -> new AppException("Unknown movie genre", HttpStatus.NOT_FOUND));

    String deleteJoinTableQuery = "DELETE FROM movie_genre_map WHERE genre_id = ?";
    Query query = entityManager.createNativeQuery(deleteJoinTableQuery);
    query.setParameter(1, id);
    query.executeUpdate();

    try {
      movieGenreRepository.delete(movieGenre);
    }
    catch (DataIntegrityViolationException ex)
    {
      throw new AppException("Cannot delete movie genre", HttpStatus.BAD_REQUEST);
    }
  }
}