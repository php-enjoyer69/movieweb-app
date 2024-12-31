package movieweb.movieweb.dtos.movies;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movieweb.movieweb.entities.MovieGenre;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
  @NotNull(message = "Id is required")
  @Id
  private Long id;

  @NotEmpty(message = "Title is required")
  @Size(min = 2, max = 50, message = "The title must be between 2 and 50 characters")
  private String title;

  @NotEmpty(message = "Description is required")
  @Size(min = 2, max = 1000, message = "Description must be between 2 and 1000 characters long")
  private String description;

  @NotNull(message = "Year is required")
  @Min(value = 1900, message = "Year must be at least 1900")
  @Max(value = 2025, message = "Year must be at most 2025")
  private Integer year;

  private Double averageRating;

  @NotNull(message = "At least one genre is required")
  private Set<MovieGenre> genres;

  @NotEmpty(message = "Image is required")
  private String img;
}
