package movieweb.movieweb.dtos.movieGenres;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieGenreDto
{
  @NotNull(message = "Id is required")
  @Id
  private Long id;

  @NotEmpty(message = "Name is required")
  @Size(min = 2, max = 50, message = "The name must be between 2 and 50 characters long")
  private String name;
}
