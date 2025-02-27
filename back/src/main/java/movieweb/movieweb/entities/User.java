package movieweb.movieweb.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import movieweb.movieweb.enums.UserRole;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(min = 2, max = 50)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Size(min = 2, max = 50)
    @Email
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 8, max = 100)
    private String password;

    @Column(name = "img")
    private String img;

    @OneToMany(mappedBy = "user")
    private Set<FavoriteMovie> favoriteMovies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WatchlistMovie> watchlistMovies = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
