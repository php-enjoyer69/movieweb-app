package movieweb.movieweb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import movieweb.movieweb.entities.Movie;

public interface MovieRepositoryCustom
{
  Page<Movie> findAll(String searchParam, String searchValue, Pageable pageable);
}
