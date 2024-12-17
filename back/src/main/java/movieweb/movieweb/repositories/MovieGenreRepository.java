package movieweb.movieweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import movieweb.movieweb.entities.MovieGenre;

import java.util.Optional;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Long>
{
  Optional<MovieGenre> findByName(String name);
  MovieGenre getByName(String name);
}
