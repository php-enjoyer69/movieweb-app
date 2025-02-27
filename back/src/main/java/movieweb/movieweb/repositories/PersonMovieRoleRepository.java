package movieweb.movieweb.repositories;

import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.entities.PersonMovieRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonMovieRoleRepository extends JpaRepository<PersonMovieRole, Long> {
    List<PersonMovieRole> findByMovie(Movie movie);
    List<PersonMovieRole> findByPerson(Person person);
    List<PersonMovieRole> findByMovieId(Long movieId);
    List<PersonMovieRole> findByPersonId(Long personId);
}
