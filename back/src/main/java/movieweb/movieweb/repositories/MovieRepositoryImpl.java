package movieweb.movieweb.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import movieweb.movieweb.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

public class MovieRepositoryImpl implements MovieRepositoryCustom
{
  @PersistenceContext
  EntityManager entityManager;

  @Override
  public Page<Movie> findAll(String searchParam, String searchValue, Pageable pageable)
  {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
    Root<Movie> movie = criteriaQuery.from(Movie.class);

    Expression serachColumn = movie.get(searchParam);
    Class columnType = movie.get(searchParam).getModel().getBindableJavaType();


    criteriaQuery.select(movie);

    if (columnType.isAssignableFrom(String.class))
      criteriaQuery.where(
          criteriaBuilder.like(
              serachColumn,
              "%" + searchValue + "%"
          )
      );
    else
      criteriaQuery.where(
          criteriaBuilder.equal(
              serachColumn,
              searchValue
          )
      );

    criteriaQuery.orderBy(
        QueryUtils.toOrders(
            pageable.getSort(),
            movie,
            criteriaBuilder
        )
    );

    TypedQuery<Movie> query = entityManager.createQuery(criteriaQuery);

    int count = query.getResultList().size();

    query.setMaxResults(pageable.getPageSize());
    query.setFirstResult((int) pageable.getOffset());

    return new PageImpl<>(query.getResultList(), pageable, count);
  }
}
