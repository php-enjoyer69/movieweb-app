package movieweb.movieweb.mappers;

import movieweb.movieweb.dtos.persons.NewPersonDto;
import movieweb.movieweb.dtos.persons.PatchPersonDto;
import movieweb.movieweb.dtos.persons.PersonDto;
import movieweb.movieweb.dtos.roles.PersonMovieRoleDto;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.entities.PersonMovieRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.BeanMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(source = "averageRating", target = "averageRating")
    @Mapping(source = "voteCount", target = "voteCount")
    @Mapping(source = "gender", target = "gender")
    @Mapping(target = "roles", source = "roles")
    PersonDto toPersonDto(Person person);

    @Mapping(target = "voteCount", constant = "0")
    Person newPersonDtoToPerson(NewPersonDto newPersonDto);

    List<PersonDto> toPersonDtoList(List<Person> persons);

    default List<PersonMovieRoleDto> mapRoles(Set<PersonMovieRole> personMovieRoles) {
        if (personMovieRoles == null) {
            return null;
        }
        return personMovieRoles.stream()
                .map(role -> PersonMovieRoleDto.builder()
                        .movieId(role.getMovie().getId())
                        .movieTitle(role.getMovie().getTitle())
                        .role(role.getRole().name())
                        .name(role.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Person person, PatchPersonDto patchPersonDto);
}
