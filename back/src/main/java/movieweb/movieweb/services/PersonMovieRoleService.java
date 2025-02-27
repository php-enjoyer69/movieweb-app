package movieweb.movieweb.services;

import movieweb.movieweb.dtos.roles.PersonMovieRoleDto;
import movieweb.movieweb.entities.PersonMovieRole;
import movieweb.movieweb.repositories.PersonMovieRoleRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.List;

@Service
public class PersonMovieRoleService {

    private final PersonMovieRoleRepository personMovieRoleRepository;

    public PersonMovieRoleService(PersonMovieRoleRepository personMovieRoleRepository) {
        this.personMovieRoleRepository = personMovieRoleRepository;
    }

    public List<PersonMovieRole> getRolesByPersonId(Long personId) {
        return personMovieRoleRepository.findByPersonId(personId);
    }

    public List<PersonMovieRole> getRolesByMovieId(Long movieId) {
        return personMovieRoleRepository.findByMovieId(movieId);
    }

    public PersonMovieRole addPersonMovieRole(PersonMovieRole personMovieRole) {
        return personMovieRoleRepository.save(personMovieRole);
    }
}