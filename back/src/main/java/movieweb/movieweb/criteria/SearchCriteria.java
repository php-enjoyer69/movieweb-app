package movieweb.movieweb.criteria;

import lombok.*;
import movieweb.movieweb.enums.SearchOperation;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SearchCriteria {

  @NonNull private String key; // Ensures 'key' cannot be null
  @NonNull private SearchOperation operation; // Ensures 'operation' cannot be null
  @NonNull private Object value; // Ensures 'value' cannot be null
  @Getter private boolean orPredicate;

  // Static method to parse search string and build criteria
  public static List<SearchCriteria> parseSearch(String search) {
    List<SearchCriteria> params = new ArrayList<>();

    // Split the search by OR predicate (|)
    for (String part : search.split("\\|")) {
      boolean isOrPredicate = params.size() > 0;  // Treat the first group as AND, subsequent as OR

      // Split the part into AND conditions (comma-separated)
      for (String criterion : part.split(",")) {
        String[] keyValue = criterion.split(":");

        if (keyValue.length == 2) {
          String key = keyValue[0].trim();
          String value = keyValue[1].trim();

          // Default operation set to LIKE for all criteria
          SearchOperation operation = SearchOperation.LIKE; // Adjust this as necessary, e.g., from query input

          // Build the SearchCriteria with non-null parameters
          SearchCriteria criteria = SearchCriteria.builder()
                  .key(key)
                  .operation(operation)
                  .value(value)
                  .orPredicate(isOrPredicate)
                  .build();

          params.add(criteria);
        } else {
          // Handle invalid or empty criteria (log or throw an exception)
          System.out.println("Invalid criterion: " + criterion);
        }
      }
    }
    return params;
  }
}
