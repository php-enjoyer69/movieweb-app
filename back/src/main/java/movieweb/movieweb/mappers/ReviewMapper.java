package movieweb.movieweb.mappers;

import movieweb.movieweb.dtos.reviews.PatchReviewDto;
import movieweb.movieweb.dtos.reviews.ReviewDto;
import movieweb.movieweb.dtos.reviews.NewReviewDto;
import movieweb.movieweb.entities.Review;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDto toReviewDto(Review review);

    List<ReviewDto> toReviewDtoList(List<Review> reviews);

    Review toReview(NewReviewDto newReviewDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Review review, PatchReviewDto patchReviewDto);
}
