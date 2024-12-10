package movieweb.movieweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import movieweb.movieweb.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom
{
    Optional<User> findByEmail(String email);
}
