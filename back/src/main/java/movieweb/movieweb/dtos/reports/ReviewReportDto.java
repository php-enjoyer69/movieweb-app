package movieweb.movieweb.dtos.reports;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewReportDto {
    private Long id;

    private Long reviewId;

    private Long userId;

    private String userName;

    private LocalDateTime reportedAt;
}
