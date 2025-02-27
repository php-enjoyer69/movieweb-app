package movieweb.movieweb.dtos.roles;

import jakarta.persistence.Column;
import movieweb.movieweb.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RoleAssignmentRequest {
    private Long personId;
    private Role role;
    private String name;
}
