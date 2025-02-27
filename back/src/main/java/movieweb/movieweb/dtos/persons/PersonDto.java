package movieweb.movieweb.dtos.persons;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movieweb.movieweb.dtos.roles.PersonMovieRoleDto;
import movieweb.movieweb.entities.PersonMovieRole;
import movieweb.movieweb.enums.Gender;
import movieweb.movieweb.enums.Role;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonDto {

    @NotNull(message = "Id is required")
    @Id
    private Long id;

    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotEmpty(message = "Surname is required")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    private String surname;

    @Size(max = 5000)
    private String biography;
    
    private Date birthDate;

    @NotEmpty(message = "Country of origin is required")
    private String countryOfOrigin;

    @NotEmpty(message = "Image is required")
    private String img;

    private double averageRating;

    private int voteCount;

    private Gender gender;

    private List<PersonMovieRoleDto> roles;

}
