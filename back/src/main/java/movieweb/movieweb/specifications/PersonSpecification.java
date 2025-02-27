package movieweb.movieweb.specifications;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.MovieGenre;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.criteria.SearchCriteria;
import movieweb.movieweb.entities.PersonMovieRole;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class PersonSpecification implements Specification<Person> {
    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Person> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate;

        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString().replace("*", "%") + "%");
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            case IN_SET -> {
                Collection<?> roleValues = List.of((String) criteria.getValue());
                Join<Person, PersonMovieRole> join = root.join("roles");
                Predicate rolePredicate = join.get("role").in(roleValues);
                yield rolePredicate;
            }
        };
    }
}

