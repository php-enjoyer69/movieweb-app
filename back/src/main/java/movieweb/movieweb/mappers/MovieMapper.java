package movieweb.movieweb.mappers;

import movieweb.movieweb.dtos.movies.NewMovieDto;
import movieweb.movieweb.entities.Movie;
import org.mapstruct.*;
import movieweb.movieweb.dtos.movies.PatchMovieDto;
import movieweb.movieweb.dtos.movies.MovieDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper
{
  MovieDto toMovieDto(Movie movie);

  @Mapping(target = "ratingCount", constant = "0")
  Movie newMovieDtoToMovie(NewMovieDto newMovieDto);
  List<MovieDto> toMovieDtoList(List<Movie> movie);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(@MappingTarget Movie movie, PatchMovieDto patchMovieDto);
}
