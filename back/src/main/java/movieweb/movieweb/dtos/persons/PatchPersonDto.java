package movieweb.movieweb.dtos.persons;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movieweb.movieweb.enums.Gender;
import movieweb.movieweb.enums.Role;


import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchPersonDto {

    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    private String surname;

    private String biography;

    private Date birthDate;

    private String countryOfOrigin;

    private String img;

    private double averageRating = 0.0;

    private int voteCount = 0;

    private Gender gender;
}
