package movieweb.movieweb.specifications;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import movieweb.movieweb.criteria.SearchCriteria;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.enums.SearchOperation;
import movieweb.movieweb.entities.Comment;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
public class UserSpecification implements Specification<User> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            case IN_SET -> {
                Collection<?> comments = (Collection<?>) criteria.getValue();
                Join<User, Comment> join = root.join("userId");
                Predicate commentPredicate = join.get("userName").in(comments);
                yield commentPredicate;
            }
        };
    }
}
