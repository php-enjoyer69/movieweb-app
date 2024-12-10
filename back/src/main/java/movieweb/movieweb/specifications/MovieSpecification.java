package movieweb.movieweb.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import movieweb.movieweb.entities.Movie;
import org.springframework.data.jpa.domain.Specification;
import movieweb.movieweb.criteria.SearchCriteria;

@AllArgsConstructor
@NoArgsConstructor
public class MovieSpecification implements Specification<Movie>
{
  private SearchCriteria criteria;
  @Override
  public Predicate toPredicate(Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder builder)
  {
    return switch(criteria.getOperation())
    {
      case EQUALITY     -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
      case NEGATION     -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
      case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
      case LESS_THAN    -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
      case LIKE         -> builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
      case STARTS_WITH  -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
      case ENDS_WITH    -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
      case CONTAINS     -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
    };
  }
}