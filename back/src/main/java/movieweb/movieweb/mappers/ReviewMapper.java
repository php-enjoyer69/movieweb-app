package movieweb.movieweb.mappers;

import movieweb.movieweb.dtos.reviews.PatchReviewDto;
import movieweb.movieweb.dtos.reviews.ReviewDto;
import movieweb.movieweb.dtos.reviews.NewReviewDto;
import movieweb.movieweb.entities.Review;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.img", target = "userImg")
    @Mapping(source = "movie.title", target = "movieTitle")
    @Mapping(source = "movie.year", target = "movieYear")
    @Mapping(source = "movie.img", target = "movieImage")
    @Mapping(source = "movie.id", target = "movieId")
    @Mapping(source = "wantsToWatchAgain", target = "wantsToWatchAgain")
    @Mapping(source = "likeCount", target = "likeCount")
    @Mapping(source = "dislikeCount", target = "dislikeCount")
    ReviewDto toReviewDto(Review review);

    List<ReviewDto> toReviewDtoList(List<Review> reviews);

    @Mapping(target = "wantsToWatchAgain", source = "wantsToWatchAgain")
    Review toReview(NewReviewDto newReviewDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Review review, PatchReviewDto patchReviewDto);
}




