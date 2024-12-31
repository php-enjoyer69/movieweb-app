package movieweb.movieweb.loaders;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import movieweb.movieweb.dtos.auth.RegisterDto;
import movieweb.movieweb.entities.User;
import movieweb.movieweb.mappers.UserMapper;
import movieweb.movieweb.repositories.UserRepository;

import java.nio.CharBuffer;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PopulateUserDataLoader implements ApplicationRunner
{
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public void run(ApplicationArguments args)
  {
    populateUsers();
  }

  private void populateUsers()
  {
    RegisterDto adminDto = RegisterDto.builder()
            .email("admin@mail.com")
            .name("admin")
            .password("zaq1@WSX")
            .passwordConfirmation("zaq1@WSX")
            .build();

    createUser(adminDto);
  }

  private void createUser(RegisterDto registerDto)
  {
    Optional<User> existingUser = userRepository.findByEmail(registerDto.getEmail());

    if (!existingUser.isPresent())
    {
      User user = userMapper.registerToUser(registerDto);
      user.setPassword(passwordEncoder.encode(CharBuffer.wrap(registerDto.getPassword())));

      userRepository.save(user);
    }
  }
}
