package movieweb.movieweb.loaders;

import lombok.RequiredArgsConstructor;
import movieweb.movieweb.dtos.persons.NewPersonDto;
import movieweb.movieweb.entities.Person;
import movieweb.movieweb.enums.Gender;
import movieweb.movieweb.mappers.PersonMapper;
import movieweb.movieweb.repositories.PersonRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class PopulatePersonDataLoader implements ApplicationRunner {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final PopulatedRoleDataLoader populatedRoleDataLoader;

    @Override
    public void run(ApplicationArguments args) {
        populatePersons();
        populatedRoleDataLoader.assignRolesToMovies();
    }

    private void populatePersons() {
        String[] names = {
                "John", "Jane", "Michael", "Sarah", "David", "Emma", "Robert", "Olivia",
                "William", "Sophia", "James", "Emily", "Daniel", "Isabella", "Matthew",
                "Charlotte", "Alexander", "Ava", "Benjamin", "Amelia", "Lucas", "Mia",
                "Ethan", "Grace", "Oliver", "Harper", "Henry", "Lily", "Samuel", "Zoe",
                "Jacob", "Chloe", "Andrew", "Madison", "Isaac", "Ella", "Nathan", "Victoria",
                "Joseph", "Abigail", "Matthew", "Ella", "Jackson", "Ella", "Mason", "Scarlett"
        };

        String[] surnames = {
                "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
                "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Perez", "Wilson",
                "Taylor", "Moore", "Jackson", "Martin", "Lee", "White", "Clark", "Lewis",
                "Walker", "Young", "Hall", "Allen", "King", "Scott", "Adams", "Baker",
                "Nelson", "Carter", "Mitchell", "Perez", "Roberts", "Evans", "Collins",
                "Stewart", "Morris", "Murphy", "Cook", "Price", "Rogers", "Gutierrez",
                "Morgan", "Powell", "Diaz"
        };

        String[] countries = {"United States of America", "United Kingdom", "Canada", "Australia", "Germany", "France", "Spain", "Italy", "India", "Mexico"};

        Gender[] genders = Gender.values(); // Pobiera wszystkie wartości z enumu Gender

        Random rand = new Random();

        for (int i = 0; i < 5; i++) {
            String name = names[rand.nextInt(names.length)];
            String surname = surnames[rand.nextInt(surnames.length)];
            String country = countries[rand.nextInt(countries.length)];
            Gender gender = genders[rand.nextInt(genders.length)]; // Losowa płeć
            String img = "person" + (i + 1) + ".jpg";

            Calendar calendar = Calendar.getInstance();
            calendar.set(1999, Calendar.JANUARY, 17);

            NewPersonDto newPersonDto = NewPersonDto.builder()
                    .name(name)
                    .surname(surname)
                    .biography("Emerging as a force to be reckoned with, on both the big and small screen. Her most prominent roles include the series Bates Motel (2013), and the films The Last Airbender (2010) and Transformers: Age of Extinction (2014).")
                    .birthDate(calendar.getTime())
                    .countryOfOrigin(country)
                    .gender(gender)
                    .img(img)
                    .build();

            Person person = personMapper.newPersonDtoToPerson(newPersonDto);
            personRepository.save(person);
        }
    }
}
