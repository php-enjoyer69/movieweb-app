package movieweb.movieweb.dtos.reports;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentReportDto {
    private Long id;
    private Long commentId;
    private Long userId;
    private String userName;
    private LocalDateTime reportedAt;
}
