package movieweb.movieweb.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import movieweb.movieweb.dtos.movieGenres.NewMovieGenreDto;
import movieweb.movieweb.dtos.movieGenres.PatchMovieGenreDto;
import movieweb.movieweb.dtos.movieGenres.MovieGenreDto;
import movieweb.movieweb.entities.MovieGenre;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieGenreMapper
{
  MovieGenreDto toMovieGenreDto(MovieGenre movieGenre);

  MovieGenre toMovieGenre(NewMovieGenreDto newMovieGenreDto);

  List<MovieGenreDto> toMovieGenreDtoList(List<MovieGenre> movieGenres);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void update(@MappingTarget MovieGenre movieGenre, PatchMovieGenreDto patchMovieGenreDto);
}