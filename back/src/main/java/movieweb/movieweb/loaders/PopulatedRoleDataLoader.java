package movieweb.movieweb.loaders;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movieweb.movieweb.entities.Movie;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.entities.PersonMovieRole;
import movieweb.movieweb.enums.Role;
import movieweb.movieweb.repositories.MovieRepository;
import movieweb.movieweb.repositories.PersonRepository;
import movieweb.movieweb.repositories.PersonMovieRoleRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PopulatedRoleDataLoader {

    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;
    private final PersonMovieRoleRepository personMovieRoleRepository;

    @Transactional
    public void assignRolesToMovies() {
        List<Movie> allMovies = movieRepository.findAll();
        List<Person> allPersons = personRepository.findAll();

        Random rand = new Random();

        for (Movie movie : allMovies) {
            Set<Person> assignedPersons = new HashSet<>();

            Person director = getRandomPerson(allPersons, assignedPersons);
            PersonMovieRole directorRole = PersonMovieRole.builder()
                    .person(director)
                    .movie(movie)
                    .role(Role.director)
                    .build();
            personMovieRoleRepository.save(directorRole);
            assignedPersons.add(director);

            Person producer = getRandomPerson(allPersons, assignedPersons);
            PersonMovieRole producerRole = PersonMovieRole.builder()
                    .person(producer)
                    .movie(movie)
                    .role(Role.producer)
                    .build();
            personMovieRoleRepository.save(producerRole);
            assignedPersons.add(producer);

            Person screenwriter = getRandomPerson(allPersons, assignedPersons);
            PersonMovieRole screenwriterRole = PersonMovieRole.builder()
                    .person(screenwriter)
                    .movie(movie)
                    .role(Role.screenwriter)
                    .build();
            personMovieRoleRepository.save(screenwriterRole);
            assignedPersons.add(screenwriter);

            for (int i = 0; i < 2; i++) {
                Person actor = getRandomPerson(allPersons, assignedPersons);
                PersonMovieRole actorRole = PersonMovieRole.builder()
                        .person(actor)
                        .movie(movie)
                        .role(Role.actor)
                        .name("Jack Sparrow")
                        .build();
                personMovieRoleRepository.save(actorRole);
                assignedPersons.add(actor);
            }
        }
    }

    private Person getRandomPerson(List<Person> allPersons, Set<Person> assignedPersons) {
        Random rand = new Random();
        Person person;
        do {
            person = allPersons.get(rand.nextInt(allPersons.size()));
        } while (assignedPersons.contains(person));
        return person;
    }
}

