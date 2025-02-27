package movieweb.movieweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import movieweb.movieweb.entities.Movie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository
extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie>, MovieRepositoryCustom {
  Optional<Movie> findByTitle(String title);

}
