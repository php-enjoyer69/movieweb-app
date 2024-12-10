package movieweb.movieweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import movieweb.movieweb.entities.Image;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long>
{
  Optional<Image> findByName(String name);
}
