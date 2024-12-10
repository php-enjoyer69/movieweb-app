package movieweb.movieweb.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import movieweb.movieweb.dtos.users.UserDto;

public interface UserRepositoryCustom
{
  Page<UserDto> findAll(String searchParam, String searchValue, Pageable pageable);
}
