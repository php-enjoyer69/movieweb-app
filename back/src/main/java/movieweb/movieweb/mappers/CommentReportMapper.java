package movieweb.movieweb.mappers;

import movieweb.movieweb.dtos.reports.CommentReportDto;
import movieweb.movieweb.entities.CommentReport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentReportMapper {

    @Mapping(source = "comment.id", target = "commentId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "reportedAt", target = "reportedAt")
    @Mapping(source = "user.name", target = "userName")
    CommentReportDto toDto(CommentReport report);

    List<CommentReportDto> toDtoList(List<CommentReport> reports);
}
