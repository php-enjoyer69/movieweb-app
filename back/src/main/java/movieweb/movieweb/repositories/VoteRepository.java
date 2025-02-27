package movieweb.movieweb.repositories;

import movieweb.movieweb.entities.Person;
import movieweb.movieweb.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByPersonIdAndUserId(Long personId, Long userId);

    List<Vote> findByPersonId(Long personId);

    @Query("SELECT v.person FROM Vote v WHERE v.userId = :userId")
    List<Person> findPersonsVotedByUser(@Param("userId") Long userId);
}
