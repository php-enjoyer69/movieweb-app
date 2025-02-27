package movieweb.movieweb.dtos.comments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private Long personId;
    private Long userId;
    private String userName;
    private String userImg;
    private String content;
    private LocalDateTime createdAt;
    private Boolean edited;
    private Long parentCommentId;
    private List<CommentDto> replies;
}
