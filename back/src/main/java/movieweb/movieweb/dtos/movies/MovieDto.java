package movieweb.movieweb.dtos.movies;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
  @NotNull(message = "Id is required")
  private Long id;

  @NotEmpty(message = "Title is required")
  @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters")
  private String title;

  @NotEmpty(message = "Description is required")
  @Size(min = 2, max = 1000, message = "Description must be between 2 and 1000 characters long")
  private String description;

  @NotNull(message = "Year is required")
  @Min(value = 1000, message = "Year must be at least 1000")
  @Max(value = 2025, message = "Year must be at most 2025")
  private Integer year;

  @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be a non-negative number")
  @DecimalMax(value = "10.0", inclusive = true, message = "Rating must be at most 10.0")
  private Double rating; // Nullable field

  @NotEmpty(message = "Image URL is required")
  private String img;
}