package movieweb.movieweb.specifications;

import org.springframework.data.jpa.domain.Specification;
import movieweb.movieweb.criteria.SearchCriteria;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.enums.SearchOperation;

import java.util.ArrayList;
import java.util.List;

public class MovieSpecificationsBuilder
{
  private List<SearchCriteria> params = new ArrayList<>();

  public MovieSpecificationsBuilder with(String key, String operation, Object value, String prefix, String suffix)
  {
    SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));

    if (op == null)
      return this;

    if (op == SearchOperation.EQUALITY)
    {
      boolean startWithAsterisk = prefix.contains("*");
      boolean endWithAsterisk = suffix.contains("*");

      if (startWithAsterisk && endWithAsterisk)
        op = SearchOperation.CONTAINS;
      else if (startWithAsterisk)
        op = SearchOperation.ENDS_WITH;
      else if (endWithAsterisk)
        op = SearchOperation.STARTS_WITH;
    }

    params.add(new SearchCriteria(key, op, value));
    return this;
  }

  public Specification<Movie> build()
  {
    if (params.isEmpty())
      return null;

    Specification result = new MovieSpecification(params.get(0));

    for (int i = 1; i < params.size(); i++)
    {
      result = params.get(i).isOrPredicate()
              ? Specification.where(result).or(new MovieSpecification(params.get(i)))
              : Specification.where(result).and(new MovieSpecification(params.get(i)));
    }

    return result;
  }

}