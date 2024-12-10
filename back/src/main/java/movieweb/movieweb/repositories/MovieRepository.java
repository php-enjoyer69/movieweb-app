package movieweb.movieweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import movieweb.movieweb.entities.Movie;

import java.util.Optional;

public interface MovieRepository
extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie>, MovieRepositoryCustom {
  Optional<Movie> findByTitle(String title);
}
