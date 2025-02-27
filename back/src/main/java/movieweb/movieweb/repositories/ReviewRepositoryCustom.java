package movieweb.movieweb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import movieweb.movieweb.entities.Review;

public interface ReviewRepositoryCustom
{
    Page<Review> findAll(String searchParam, String searchValue, Pageable pageable);
}
