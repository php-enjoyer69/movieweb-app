package movieweb.movieweb.services;

import com.google.common.base.Joiner;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.persons.NewPersonDto;
import movieweb.movieweb.dtos.persons.PersonDto;
import movieweb.movieweb.dtos.persons.PatchPersonDto;
import movieweb.movieweb.dtos.persons.UserVoteStatusDto;
import movieweb.movieweb.dtos.roles.PersonMovieRoleDto;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.entities.PersonMovieRole;
import movieweb.movieweb.entities.Vote;
import movieweb.movieweb.enums.SearchOperation;
import movieweb.movieweb.enums.VoteType;
import movieweb.movieweb.exceptions.AppException;
import movieweb.movieweb.mappers.PersonMapper;
import movieweb.movieweb.repositories.PersonMovieRoleRepository;
import movieweb.movieweb.repositories.PersonRepository;
import movieweb.movieweb.repositories.VoteRepository;
import movieweb.movieweb.specifications.MovieSpecificationsBuilder;
import movieweb.movieweb.specifications.PersonSpecificationsBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final VoteRepository voteRepository;
    private final PersonMapper personMapper;
    private final PersonMovieRoleRepository personMovieRoleRepository;
    @PersistenceContext
    EntityManager entityManager;

    public PersonDto findById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new AppException("Unknown person", HttpStatus.NOT_FOUND));

        PersonDto personDto = personMapper.toPersonDto(person);

        return personDto;
    }

    public Page<Person> findAll(Pageable pageable, String search) {
        PersonSpecificationsBuilder builder = new PersonSpecificationsBuilder();

        if (search != null) {
            search = search.replace("%7C", "|");

            String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
            Pattern pattern = Pattern.compile(
                    "(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),"
            );

            Matcher matcher = pattern.matcher(search + ",");

            while (matcher.find()) {
                builder.with(
                        matcher.group(1),
                        matcher.group(2),
                        matcher.group(4),
                        matcher.group(3),
                        matcher.group(5)
                );
            }
        }

        Specification<Person> personSpecification = builder.build();
        Page<Person> persons = personRepository.findAll(personSpecification, pageable);

        return persons;
    }

    public List<Person> searchPersons(String name, String surname) {
        Specification<Person> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.like(root.get("name"), "%" + name + "%"));
        }

        if (surname != null && !surname.isEmpty()) {
            spec = spec.and((root, query, builder) -> builder.like(root.get("surname"), "%" + surname + "%"));
        }

        return personRepository.findAll(spec);
    }

    public PersonDto save(NewPersonDto newPersonDto) throws IOException {
        Person person = personMapper.newPersonDtoToPerson(newPersonDto);
        Person savedPerson = personRepository.save(person);

        return personMapper.toPersonDto(savedPerson);
    }

    public PersonDto update(PatchPersonDto patchPersonDto, String img, Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new AppException("Unknown person", HttpStatus.NOT_FOUND));

        if (img != null) {
            person.setImg(img);
        }

        personMapper.update(person, patchPersonDto);
        Person updatedPerson = personRepository.save(person);

        return personMapper.toPersonDto(updatedPerson);
    }

    @Transactional
    public Person delete(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new AppException("Unknown person", HttpStatus.NOT_FOUND));

        String deleteVotesQuery = "DELETE FROM person_votes WHERE person_id = ?";
        Query queryVotes = entityManager.createNativeQuery(deleteVotesQuery);
        queryVotes.setParameter(1, id);
        queryVotes.executeUpdate();

        String deleteJoinTableQuery = "DELETE FROM person_movie_roles WHERE person_id = ?";
        Query queryRoles = entityManager.createNativeQuery(deleteJoinTableQuery);
        queryRoles.setParameter(1, id);
        queryRoles.executeUpdate();

        try {
            personRepository.delete(person);
        } catch (DataIntegrityViolationException ex) {
            throw new AppException("Cannot delete the person", HttpStatus.BAD_REQUEST);
        }

        return person;
    }

    @Transactional
    public void vote(Long personId, Long userId, VoteType voteType) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new AppException("Person not found", HttpStatus.NOT_FOUND));

        Vote existingVote = voteRepository.findByPersonIdAndUserId(personId, userId);

        if (existingVote != null) {
            if (existingVote.getVoteType() == voteType) {
                voteRepository.delete(existingVote);
            } else {
                existingVote.setVoteType(voteType);
                voteRepository.save(existingVote);
            }
        } else {
            Vote newVote = Vote.builder()
                    .person(person)
                    .userId(userId)
                    .voteType(voteType)
                    .build();
            voteRepository.save(newVote);
        }

        updateAverageRating(person);
    }

    private void updateAverageRating(Person person) {
        List<Vote> votes = voteRepository.findByPersonId(person.getId());

        int totalScore = votes.stream()
                .mapToInt(vote -> vote.getVoteType() == VoteType.UPVOTE ? 1 : -1)
                .sum();

        int totalVotes = votes.size();

        person.setAverageRating((double) totalScore);
        person.setVoteCount(totalVotes);

        personRepository.save(person);
    }


    public List<PersonDto> findPersonsVotedByUser(Long userId) {
        List<Person> persons = voteRepository.findPersonsVotedByUser(userId);
        return persons.stream().map(personMapper::toPersonDto).toList();
    }

    public UserVoteStatusDto getUserVoteStatus(Long personId, Long userId) {
        Vote existingVote = voteRepository.findByPersonIdAndUserId(personId, userId);

        if (existingVote != null) {
            return new UserVoteStatusDto(true, existingVote.getVoteType());
        }

        return new UserVoteStatusDto(false, null);
    }

    public PersonDto getPersonWithRoles(Long personId) {
        Person person = personRepository.findById(personId).orElseThrow(() -> new RuntimeException("Person not found"));

        List<PersonMovieRole> personMovieRoles = personMovieRoleRepository.findByPersonId(personId);

        List<PersonMovieRoleDto> roles = personMovieRoles.stream()
                .map(role -> PersonMovieRoleDto.builder()
                        .movieId(role.getMovie().getId())
                        .movieTitle(role.getMovie().getTitle())
                        .role(role.getRole().name())
                        .build())
                .collect(Collectors.toList());

        PersonDto personDto = personMapper.toPersonDto(person);
        personDto.setRoles(roles);

        return personDto;
    }
}