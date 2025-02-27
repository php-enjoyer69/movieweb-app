package movieweb.movieweb.services;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.reports.CommentReportDto;
import movieweb.movieweb.entities.Comment;
import movieweb.movieweb.entities.CommentReport;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.mappers.CommentReportMapper;
import movieweb.movieweb.repositories.CommentReportRepository;
import movieweb.movieweb.repositories.CommentRepository;
import movieweb.movieweb.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentReportService {

    private final CommentReportRepository commentReportRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentReportMapper commentReportMapper;

    @Transactional
    public CommentReportDto reportComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<CommentReport> existingReport = commentReportRepository.findByCommentAndUser(comment, user);
        if (existingReport.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User has already reported this comment");
        }

        CommentReport report = new CommentReport();
        report.setComment(comment);
        report.setUser(user);
        commentReportRepository.save(report);

        return commentReportMapper.toDto(report);
    }

    public List<CommentReportDto> getAllReports() {
        return commentReportMapper.toDtoList(commentReportRepository.findAll());
    }

    public boolean doesReportExist(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<CommentReport> report = commentReportRepository.findByCommentAndUser(comment, user);
        return report.isPresent();
    }

    public void deleteReport(Long reportId) {
        CommentReport report = commentReportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));

        commentReportRepository.delete(report);
    }

    @Transactional
    public void deleteReportsByCommentId(Long commentId) {
        commentReportRepository.deleteByCommentId(commentId);
    }
}
