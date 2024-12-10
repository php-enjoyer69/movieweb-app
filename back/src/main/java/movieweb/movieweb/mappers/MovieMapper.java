package movieweb.movieweb.mappers;

import movieweb.movieweb.dtos.movies.NewMovieDto;
import movieweb.movieweb.entities.Movie;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import movieweb.movieweb.dtos.movies.PatchMovieDto;
import movieweb.movieweb.dtos.movies.MovieDto;

@Mapper(componentModel = "spring")
public interface MovieMapper
{
  MovieDto toMovieDto(Movie movie);
  Movie newMovieDtoToMovie(NewMovieDto newMovieDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(@MappingTarget Movie movie, PatchMovieDto patchMovieDto);
}
