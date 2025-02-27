package movieweb.movieweb.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserDto
{
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 2, max = 50, message = "Username must be from 2 to 50 characters long")
    private String name;

    @NotEmpty(message = "Email cannot be empty")
    @Size(min = 2, max = 50, message = "Email must be from 2 to 50 characters long")
    @Email(message = "Invalid email address")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, max = 100, message = "Password must be from 8 to 100 characters long")
    private String password;
}