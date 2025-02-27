package movieweb.movieweb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import movieweb.movieweb.entities.Person;

public interface PersonRepositoryCustom
{
    Page<Person> findAll(String searchParam, String searchValue, Pageable pageable);
}
