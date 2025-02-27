package movieweb.movieweb.dtos.roles;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PersonMovieRoleDto {
    private Long movieId;
    private String movieTitle;
    private String role;
    private String name;
}
