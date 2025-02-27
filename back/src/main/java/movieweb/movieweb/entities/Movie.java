package movieweb.movieweb.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "movies")
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "title", nullable = false)
  @Size(min = 2, max = 50)
  private String title;

  @Column(name = "description", nullable = false)
  @Size(min = 2, max = 1000)
  private String description;

  @Column(name = "year", nullable = false)
  @Min(value = 1000, message = "Year must be at least 1000")
  @Max(value = 2025, message = "Year must be at most 2025")
  private Integer year;

  @Column(name = "average_rating")
  private Double averageRating;

  @Column(name = "rating_count")
  private Integer ratingCount = 0;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "movie_genre_map",
          joinColumns = @JoinColumn(name = "movie_id"),
          inverseJoinColumns = @JoinColumn(name = "genre_id")
  )
  private Set<MovieGenre> genres = new HashSet<>();

  @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<PersonMovieRole> roles = new HashSet<>();

  @Column(name = "img", nullable = false)
  private String img;
}
