package movieweb.movieweb.controllers;

import movieweb.movieweb.dtos.users.PatchUserDto;
import movieweb.movieweb.dtos.users.UserDto;
import movieweb.movieweb.providers.UserAuthenticationProvider;
import movieweb.movieweb.services.ImageService;
import movieweb.movieweb.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import movieweb.movieweb.entities.Image;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Validated
public class UserController {
    private final UserService userService;
    private final ImageService imageService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getOne(@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> getAll(Pageable pageable, @RequestParam(required = false) String search) {
        Page<UserDto> userDtos = userService.findAll(pageable, search);
        userDtos.getContent().forEach(userDto -> {
            userDto.setToken(userAuthenticationProvider.createToken(userDto.getEmail(), userDto.getRole()));
        });
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping("/user")
    public ResponseEntity<UserDto> add(
            @RequestPart("user") @Valid PatchUserDto patchUserDto,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        Image savedImage = imageService.upload(image);
        patchUserDto.setImg(savedImage.getName());
        UserDto userDto = userService.save(patchUserDto);
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<UserDto> update(
            @RequestPart(value = "user", required = false) @Valid PatchUserDto patchUserDto,
            @RequestPart(value = "image", required = false) MultipartFile patchImage,
            @PathVariable Long id
    ) throws IOException {
        String imageName = null;

        if (patchImage != null) {
            UserDto userDto = userService.findById(id);
            Image updatedImage = imageService.update(userDto.getImg(), patchImage);
            imageName = updatedImage.getName();
        }

        UserDto updatedUser = userService.update(patchUserDto, imageName, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/user/{id}")
    public void delete(@PathVariable Long id) {
        UserDto userDto = userService.findById(id);
        imageService.delete(userDto.getImg());
        userService.delete(id);
    }
}