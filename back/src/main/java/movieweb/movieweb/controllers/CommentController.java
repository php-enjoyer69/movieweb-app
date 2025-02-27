package movieweb.movieweb.controllers;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.comments.CommentDto;
import movieweb.movieweb.dtos.comments.NewCommentDto;
import movieweb.movieweb.dtos.comments.PatchCommentDto;
import movieweb.movieweb.dtos.reviews.ReviewDto;
import movieweb.movieweb.services.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/comments/search")
    public ResponseEntity<List<CommentDto>> searchCommentsByUserName(@RequestParam String userName) {
        return ResponseEntity.ok(commentService.getCommentsByUserName(userName));
    }

    @PostMapping("/person/{personId}/comment/{userId}")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long personId,
            @PathVariable Long userId,
            @RequestBody NewCommentDto newCommentDto
    ) {
        return ResponseEntity.ok(commentService.addComment(personId, userId, newCommentDto));
    }

    // Dodanie odpowiedzi na komentarz
    @PostMapping("/comment/{commentId}/reply/{userId}")
    public ResponseEntity<CommentDto> addReply(
            @PathVariable Long commentId,
            @PathVariable Long userId,
            @RequestBody NewCommentDto newCommentDto,
            @RequestParam Long personId // Dodać personId jako parametru
    ) {
        // Przekazanie personId w metodzie addReply
        return ResponseEntity.ok(commentService.addReply(commentId, userId, personId, newCommentDto));
    }

    // Aktualizacja komentarza
    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody PatchCommentDto patchCommentDto
    ) {
        return ResponseEntity.ok(commentService.updateComment(commentId, patchCommentDto));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // Pobieranie wszystkich komentarzy
    @GetMapping("/comments")
    public ResponseEntity<Page<CommentDto>> getAllComments(Pageable pageable) {
        Page<CommentDto> comments = commentService.findAll(pageable);
        return ResponseEntity.ok(comments);
    }

    // Pobieranie komentarzy dla konkretnej osoby
    @GetMapping("/person/{personId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPerson(@PathVariable Long personId) {
        return ResponseEntity.ok(commentService.getCommentsByPerson(personId));
    }

    // Pobieranie komentarzy dla konkretnego użytkownika
    @GetMapping("/user/{userId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(commentService.getCommentsByUser(userId));
    }

    // Sprawdzanie, czy użytkownik skomentował daną osobę
    @GetMapping("/person/{personId}/user/{userId}/has-commented")
    public ResponseEntity<Boolean> hasUserCommentedOnPerson(
            @PathVariable Long personId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(commentService.hasUserCommentedOnPerson(personId, userId));
    }
}
