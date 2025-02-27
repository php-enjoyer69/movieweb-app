package movieweb.movieweb.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movieweb.movieweb.enums.Gender;

import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Size(max = 5000)
    @Column(name = "biography")
    private String biography;

    @Column(name = "birth_date", nullable = true)
    private Date birthDate;

    @Column(name = "country_of_origin")
    private String countryOfOrigin;

    @Column(name = "img", nullable = false)
    private String img;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PersonMovieRole> roles;

    @OneToMany(mappedBy = "person")
    private Set<Vote> votes = new HashSet<>();

    @Column(name = "average_rating", nullable = true)
    private Double averageRating = 0.0;

    @Column(name = "vote_count", nullable = true)
    private Integer voteCount;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

}
