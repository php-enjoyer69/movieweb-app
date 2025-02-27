package movieweb.movieweb.repositories;

import movieweb.movieweb.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPersonId(Long personId);

    List<Comment> findByUserId(Long userId);

    Optional<Comment> findByPersonIdAndUserId(Long personId, Long userId);

    Optional<Comment> findById(Long id);

    @Query("SELECT c FROM Comment c WHERE c.user.name LIKE %:userName%")
    List<Comment> findByUserName(@Param("userName") String userName);
}
