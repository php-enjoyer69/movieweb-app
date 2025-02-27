package movieweb.movieweb.specifications;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import movieweb.movieweb.criteria.SearchCriteria;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.MovieGenre;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.entities.PersonMovieRole;
import movieweb.movieweb.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class MovieSpecification implements Specification<Movie> {

  private SearchCriteria criteria;

  @Override
  public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

    return switch (criteria.getOperation()) {
      case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
      case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
      case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
      case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
      case LIKE -> builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
      case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
      case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
      case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
      case IN_SET -> {
        Collection<?> genreValues = List.of((String) criteria.getValue());
        Join<Movie, MovieGenre> join = root.join("genres");
        Predicate genrePredicate = join.get("name").in(genreValues);
        yield genrePredicate;
      }
    };
  }
}
