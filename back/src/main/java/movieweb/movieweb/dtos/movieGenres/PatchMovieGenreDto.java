package movieweb.movieweb.dtos.movieGenres;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchMovieGenreDto
{
  @Size(min = 2, max = 50, message = "The name must be between 2 and 50 characters long")
  private String name;
}
