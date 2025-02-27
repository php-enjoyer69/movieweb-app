package movieweb.movieweb.controllers;

import movieweb.movieweb.enums.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class PersonMovieRoleController {

    @GetMapping
    public ResponseEntity<Role[]> getRoles() {
        return ResponseEntity.ok(Role.values());
    }
}
