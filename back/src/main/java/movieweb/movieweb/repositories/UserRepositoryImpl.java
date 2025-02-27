package movieweb.movieweb.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import movieweb.movieweb.dtos.users.UserDto;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.mappers.UserMapper;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom
{
  @PersistenceContext
  EntityManager entityManager;
  UserMapper userMapper;

  @Override
  public Page<UserDto> findAll(String searchParam, String searchValue, Pageable pageable) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
    Root<User> user = criteriaQuery.from(User.class);

    List<Predicate> predicates = new ArrayList<>();

    // Handle multiple search parameters if provided
    String[] params = searchParam.split(",");
    String[] values = searchValue.split(",");

    for (int i = 0; i < params.length; i++) {
      String param = params[i];
      String value = values[i];

      Expression<?> searchColumn = user.get(param);
      Class<?> columnType = user.get(param).getModel().getBindableJavaType();

      if (columnType.isAssignableFrom(String.class)) {
        predicates.add(
                criteriaBuilder.like(searchColumn.as(String.class), "%" + value + "%")
        );
      } else {
        predicates.add(
                criteriaBuilder.equal(searchColumn, value)
        );
      }
    }

    criteriaQuery.select(user)
            .where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

    criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), user, criteriaBuilder));

    TypedQuery<User> query = entityManager.createQuery(criteriaQuery);

    int count = query.getResultList().size();
    query.setMaxResults(pageable.getPageSize());
    query.setFirstResult((int) pageable.getOffset());

    List<UserDto> userDtos = userMapper.toUserDtoList(query.getResultList());

    return new PageImpl<>(userDtos, pageable, count);
  }
}
