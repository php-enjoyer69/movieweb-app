package movieweb.movieweb.repositories;

import movieweb.movieweb.entities.CommentReport;
import movieweb.movieweb.entities.Comment;
import movieweb.movieweb.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    Optional<CommentReport> findByCommentAndUser(Comment comment, User user);

    @Modifying
    @Query("DELETE FROM CommentReport cr WHERE cr.comment.id = :commentId")
    void deleteByCommentId(@Param("commentId") Long commentId);
}
