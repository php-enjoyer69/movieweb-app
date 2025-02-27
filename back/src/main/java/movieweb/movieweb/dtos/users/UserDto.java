package movieweb.movieweb.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movieweb.movieweb.enums.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto
{
    private Long id;
    private String name;
    private String email;
    private String img;
    private String token;
    private UserRole role;
}
