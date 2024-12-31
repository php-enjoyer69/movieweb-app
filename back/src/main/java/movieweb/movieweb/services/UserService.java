package movieweb.movieweb.services;

import org.springframework.web.bind.MethodArgumentNotValidException;
import movieweb.movieweb.dtos.auth.LoginDto;
import movieweb.movieweb.dtos.auth.RegisterDto;
import movieweb.movieweb.dtos.users.PatchUserDto;
import movieweb.movieweb.dtos.users.UserDto;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.exceptions.AppException;
import movieweb.movieweb.exceptions.ExceptionUtils;
import movieweb.movieweb.mappers.UserMapper;
import movieweb.movieweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto findById(Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return userMapper.toUserDto(user);
    }

    public Page<UserDto> findAll(Pageable pageable)
    {
        Page<User> users = userRepository.findAll(pageable);
        List<UserDto> userDtos = userMapper.toUserDtoList(users.getContent());

        return new PageImpl<>(userDtos, users.getPageable(), users.getTotalElements());
    }

    public Page<UserDto> findAll(Pageable pageable, String search)
    {
        String[] searchArray = search.split(",");

        String searchParam = searchArray[0];
        String searchValue = searchArray[1];

        Page<UserDto> users = userRepository.findAll(searchParam, searchValue, pageable);

        return users;
    }

    public UserDto update(PatchUserDto patchUserDto, Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (patchUserDto.getEmail() != null && !patchUserDto.getEmail().isEmpty())
        {
            Optional<User> existingUser = userRepository.findByEmail(patchUserDto.getEmail());

            if (existingUser.isPresent())
                throw new AppException("This email address is already in use", HttpStatus.BAD_REQUEST);
        }

        if (patchUserDto.getPassword() != null && !patchUserDto.getPassword().isEmpty())
        {
            user.setPassword(passwordEncoder.encode(CharBuffer.wrap(patchUserDto.getPassword())));
        }

        userMapper.update(user, patchUserDto);

        User updatedUser = userRepository.save(user);

        return userMapper.toUserDto(updatedUser);
    }

    public void delete(Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        userRepository.delete(user);
    }

    public UserDto login(LoginDto loginDto) throws MethodArgumentNotValidException
    {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> ExceptionUtils.createValidationException("email", "Email does not exist"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
            throw ExceptionUtils.createValidationException("password", "Incorrect password");

        return userMapper.toUserDto(user);
    }

    public UserDto register(RegisterDto registerDto) throws MethodArgumentNotValidException
    {
        Optional<User> existingUser = userRepository.findByEmail(registerDto.getEmail());

        if (existingUser.isPresent())
            throw ExceptionUtils.createValidationException("email", "This email address is already in use");

        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirmation()))
            throw ExceptionUtils.createValidationException("passwordConfirmation", "Passwords do not match");

        User user = userMapper.registerToUser(registerDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(registerDto.getPassword())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public User findByEmail(String email)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return user;
    }
}
