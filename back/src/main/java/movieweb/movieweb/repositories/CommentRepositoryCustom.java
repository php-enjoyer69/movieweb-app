package movieweb.movieweb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import movieweb.movieweb.entities.Comment;

import java.util.List;

public interface CommentRepositoryCustom
{
    Page<Comment> findAll(String searchParam, String searchValue, Pageable pageable);
}
