package movieweb.movieweb.criteria;

import lombok.*;
import movieweb.movieweb.enums.SearchOperation;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SearchCriteria
{
  private String key;
  private SearchOperation operation;
  private Object value;
  @Getter
  private boolean orPredicate;

  public SearchCriteria(String key, SearchOperation operation, Object value)
  {
    this.key = key;
    this.operation = operation;
    this.value = value;
    this.orPredicate = false;
  }
}