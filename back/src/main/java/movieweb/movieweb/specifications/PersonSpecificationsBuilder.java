package movieweb.movieweb.specifications;

import movieweb.movieweb.criteria.SearchCriteria;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PersonSpecificationsBuilder {

    private final List<SearchCriteria> params;

    public PersonSpecificationsBuilder() {
        this.params = new ArrayList<>();
    }

    public PersonSpecificationsBuilder with(String key, String operation, Object value, String prefix, String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op == null) return this;

        if (op == SearchOperation.EQUALITY) {
            boolean startWithAsterisk = prefix.contains("*");
            boolean endWithAsterisk = suffix.contains("*");

            if (startWithAsterisk && endWithAsterisk) op = SearchOperation.CONTAINS;
            else if (startWithAsterisk) op = SearchOperation.ENDS_WITH;
            else if (endWithAsterisk) op = SearchOperation.STARTS_WITH;
        }

        // Obsługa operatora OR (jeśli wartość zawiera '|')
        boolean isOrPredicate = value instanceof String && value.toString().contains("|");

        if (isOrPredicate) {
            String[] values = value.toString().split("\\|");
            for (String v : values) {
                params.add(new SearchCriteria(key, op, v, true));  // Ustawiamy orPredicate na true
            }
        } else {
            params.add(new SearchCriteria(key, op, value, false)); // Domyślnie AND
        }

        return this;
    }

    public Specification<Person> build() {
        if (params.isEmpty()) {
            return null;
        }

        Specification<Person> specification = new PersonSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            SearchCriteria criteria = params.get(i);
            if (criteria.isOrPredicate()) {
                specification = Specification.where(specification).or(new PersonSpecification(criteria));
            } else {
                specification = specification.and(new PersonSpecification(criteria));
            }
        }

        return specification;
    }
}
