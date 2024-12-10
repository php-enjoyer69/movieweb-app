package movieweb.movieweb.dtos.movies;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchMovieDto {
  @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters")
  private String title;

  @Size(min = 2, max = 1000, message = "Description must be between 2 and 1000 characters long")
  private String description;

  @Min(value = 1000, message = "Year must be at least 1000")
  @Max(value = 2025, message = "Year must be at most 2025")
  private Integer year;

  @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be a non-negative number")
  @DecimalMax(value = "10.0", inclusive = true, message = "Rating must be at most 10.0")
  private Double rating;

  private String img;
}
