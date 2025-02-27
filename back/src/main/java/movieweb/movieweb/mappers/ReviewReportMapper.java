package movieweb.movieweb.mappers;

import movieweb.movieweb.dtos.reports.ReviewReportDto;
import movieweb.movieweb.entities.ReviewReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewReportMapper {

    @Mapping(source = "review.id", target = "reviewId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "reportedAt", target = "reportedAt")
    @Mapping(source = "user.name", target = "userName")
    ReviewReportDto toDto(ReviewReport report);

    List<ReviewReportDto> toDtoList(List<ReviewReport> reports);
}
