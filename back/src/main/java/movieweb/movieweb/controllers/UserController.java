package movieweb.movieweb.controllers;

import movieweb.movieweb.dtos.users.PatchUserDto;
import movieweb.movieweb.dtos.users.UserDto;
import movieweb.movieweb.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Validated
public class UserController
{
    private final UserService userService;

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getOne(@PathVariable Long id)
    {
        UserDto userDto = userService.findById(id);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getAll(Pageable pageable, @RequestParam(required = false) String search)
    {
        if (search == null || search.split(",").length != 2)
        {
            Page<UserDto> users = userService.findAll(pageable);
            return ResponseEntity.ok(users);
        }

        Page<UserDto> users = userService.findAll(pageable, search);
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<UserDto> update(@RequestBody @Valid PatchUserDto patchUserDto, @PathVariable Long id)
    {
        UserDto userDto = userService.update(patchUserDto, id);

        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable Long id)
    {
        userService.delete(id);
    }
}
