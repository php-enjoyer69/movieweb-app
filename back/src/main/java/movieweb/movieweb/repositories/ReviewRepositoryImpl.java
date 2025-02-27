package movieweb.movieweb.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import movieweb.movieweb.entities.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom
{
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Page<Review> findAll(String searchParam, String searchValue, Pageable pageable)
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> criteriaQuery = criteriaBuilder.createQuery(Review.class);
        Root<Review> review = criteriaQuery.from(Review.class);

        Expression serachColumn = review.get(searchParam);
        Class columnType = review.get(searchParam).getModel().getBindableJavaType();


        criteriaQuery.select(review);

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
                        review,
                        criteriaBuilder
                )
        );

        TypedQuery<Review> query = entityManager.createQuery(criteriaQuery);

        int count = query.getResultList().size();

        query.setMaxResults(pageable.getPageSize());
        query.setFirstResult((int) pageable.getOffset());

        return new PageImpl<>(query.getResultList(), pageable, count);
    }
}
