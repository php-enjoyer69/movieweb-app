package movieweb.movieweb.dtos.persons;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import movieweb.movieweb.enums.Gender;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPersonDto {

    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @NonNull
    private String name;

    @NotEmpty(message = "Surname is required")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    @NonNull
    private String surname;

    private String biography;

    private Date birthDate;

    @NotEmpty(message = "Country of origin is required")
    @NonNull
    private String countryOfOrigin;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String img;

    private double averageRating = 0.0;

    private int voteCount = 0;

    @NotNull
    @NonNull
    private Gender gender;
}
