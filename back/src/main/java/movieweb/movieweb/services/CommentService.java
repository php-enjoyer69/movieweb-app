package movieweb.movieweb.services;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.comments.CommentDto;
import movieweb.movieweb.dtos.comments.NewCommentDto;
import movieweb.movieweb.dtos.comments.PatchCommentDto;
import movieweb.movieweb.dtos.reviews.ReviewDto;
import movieweb.movieweb.entities.Comment;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.entities.Review;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.mappers.CommentMapper;
import movieweb.movieweb.repositories.CommentReportRepository;
import movieweb.movieweb.repositories.CommentRepository;
import movieweb.movieweb.repositories.PersonRepository;
import movieweb.movieweb.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final CommentReportRepository commentReportRepository;

    public List<CommentDto> getCommentsByUserName(String userName) {
        List<Comment> comments = commentRepository.findByUserName(userName);
        return commentMapper.toCommentDtoList(comments);
    }

    @Transactional
    public CommentDto addComment(Long personId, Long userId, NewCommentDto newCommentDto) {
        Person person = findPersonById(personId);
        User user = findUserById(userId);

        Comment comment = new Comment();
        comment.setPerson(person);
        comment.setUser(user);
        comment.setContent(newCommentDto.getContent());

        commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);
    }

    @Transactional
    public CommentDto updateComment(Long commentId, PatchCommentDto patchCommentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (patchCommentDto.getContent() != null) {
            comment.setContent(patchCommentDto.getContent());
        }

        if (patchCommentDto.getUpdatedAt() != null) {
            comment.setUpdatedAt(patchCommentDto.getUpdatedAt());
        } else {
            comment.setUpdatedAt(LocalDateTime.now()); // Domyślnie ustawiamy aktualny czas
        }

        commentRepository.save(comment);

        return commentMapper.toCommentDto(comment);
    }

    @Transactional
    public CommentDto addReply(Long commentId, Long userId, Long personId, NewCommentDto newCommentDto) {
        Person person = findPersonById(personId); // Pobranie osoby
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent comment not found"));

        User user = findUserById(userId);

        Comment reply = new Comment();
        reply.setUser(user);
        reply.setContent(newCommentDto.getContent());
        reply.setPerson(person); // Powiązanie odpowiedzi z osobą

        reply.setParentComment(parentComment);
        commentRepository.save(reply);

        return commentMapper.toCommentDto(reply);
    }

    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getCommentsByPerson(Long personId) {
        return commentRepository.findByPersonId(personId).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public List<CommentDto> getCommentsByUser(Long userId) {
        return commentRepository.findByUserId(userId).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public boolean hasUserCommentedOnPerson(Long personId, Long userId) {
        return commentRepository.findByPersonIdAndUserId(personId, userId).isPresent();
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        commentReportRepository.deleteByCommentId(commentId);

        commentRepository.delete(comment);
    }

    private Person findPersonById(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found"));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public Page<CommentDto> findAll(Pageable pageable) {
        Page<Comment> comments = commentRepository.findAll(pageable);
        return comments.map(commentMapper::toCommentDto);
    }

}
