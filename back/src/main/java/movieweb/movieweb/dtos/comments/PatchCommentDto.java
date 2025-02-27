package movieweb.movieweb.dtos.comments;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchCommentDto {

    @Size(min = 1, max = 1000, message = "Comment content must be between 1 and 1000 characters")
    private String content;

    private LocalDateTime updatedAt;

}
