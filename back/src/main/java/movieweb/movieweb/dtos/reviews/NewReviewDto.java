package movieweb.movieweb.dtos.reviews;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewReviewDto {

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "Rating must be at most 10.0")
    private Double rating;

    @Size(min = 0, max = 5000, message = "Review content must be between 0 and 5000 characters")
    private String content;

    private Boolean wantsToWatchAgain;
}
