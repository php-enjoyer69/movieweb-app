package movieweb.movieweb.dtos.reviews;

import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {

    @NotNull(message = "Id is required")
    @Id
    private Long id;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be a non-negative number")
    @DecimalMax(value = "10.0", inclusive = true, message = "Rating must be at most 10.0")
    private Double rating;

    @Size(max = 5000, message = "Content must be less than or equal to 5000 characters")
    private String content;

    private Date createdAt;

    private String userName;

    private String movieTitle;

    private Integer movieYear;

    private String movieImage;

    private String movieId;

    private Long userId;

    private String userImg;

    private Boolean wantsToWatchAgain;

    private int likeCount;

    private int dislikeCount;
}
