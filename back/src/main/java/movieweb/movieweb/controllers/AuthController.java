package movieweb.movieweb.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import movieweb.movieweb.dtos.auth.LoginDto;
import movieweb.movieweb.dtos.auth.RegisterDto;
import movieweb.movieweb.dtos.users.UserDto;
import movieweb.movieweb.providers.UserAuthenticationProvider;
import movieweb.movieweb.services.UserService;
import movieweb.movieweb.enums.UserRole; // Import enum UserRole

import java.net.URI;

@RequiredArgsConstructor
@RestController
@Validated
public class AuthController {
    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid LoginDto loginDto) throws MethodArgumentNotValidException {
        UserDto userDto = userService.login(loginDto);

        userDto.setToken(userAuthenticationProvider.createToken(userDto.getEmail(), userDto.getRole()));

        if (userDto.getRole() == null) {
            userDto.setRole(UserRole.USER);
        }

        return ResponseEntity.ok(userDto);
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid RegisterDto registerDto) throws MethodArgumentNotValidException {

        UserDto createdUser = userService.register(registerDto);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser.getEmail(), createdUser.getRole()));

        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

}
