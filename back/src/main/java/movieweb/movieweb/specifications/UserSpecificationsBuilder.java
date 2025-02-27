package movieweb.movieweb.specifications;

import movieweb.movieweb.criteria.SearchCriteria;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecificationsBuilder {

    private List<SearchCriteria> params = new ArrayList<>();

    public UserSpecificationsBuilder with(String key, String operation, Object value, String prefix, String suffix) {

        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));

        if (op == null)
            return this;

        if (op == SearchOperation.EQUALITY) {
            boolean startWithAsterisk = prefix.contains("*");
            boolean endWithAsterisk = suffix.contains("*");

            if (startWithAsterisk && endWithAsterisk)
                op = SearchOperation.CONTAINS;
            else if (startWithAsterisk)
                op = SearchOperation.ENDS_WITH;
            else if (endWithAsterisk)
                op = SearchOperation.STARTS_WITH;
        }

        params.add(SearchCriteria.builder()
                .key(key)
                .operation(op)
                .value(value)
                .build());

        return this;
    }

    public Specification<User> build() {
        if (params.isEmpty())
            return null;

        Specification<User> result = new UserSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new UserSpecification(params.get(i)))
                    : Specification.where(result).and(new UserSpecification(params.get(i)));
        }

        return result;
    }
}
