package movieweb.movieweb.mappers;

import movieweb.movieweb.dtos.auth.RegisterDto;
import movieweb.movieweb.dtos.users.NewUserDto;
import movieweb.movieweb.dtos.users.PatchUserDto;
import movieweb.movieweb.dtos.users.UserDto;
import movieweb.movieweb.entities.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    UserDto toUserDto(User user);
    List<UserDto> toUserDtoList(List<User> users);

    @Mapping(target = "password", ignore = true)
    User registerToUser(RegisterDto registerDto);

    User newUserDtoToUser(NewUserDto newUserDto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget User user, PatchUserDto patchUserDto);
}
