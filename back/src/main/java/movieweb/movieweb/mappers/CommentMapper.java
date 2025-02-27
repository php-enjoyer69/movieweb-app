package movieweb.movieweb.mappers;

import movieweb.movieweb.dtos.comments.CommentDto;
import movieweb.movieweb.dtos.comments.NewCommentDto;
import movieweb.movieweb.dtos.comments.PatchCommentDto;
import movieweb.movieweb.entities.Comment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.img", target = "userImg")
    @Mapping(source = "replies", target = "replies")
    @Mapping(target = "parentCommentId", source = "parentComment.id")  // Mapowanie parentComment na parentCommentId
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDtoList(List<Comment> comments);

    Comment toComment(NewCommentDto newCommentDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Comment comment, PatchCommentDto patchCommentDto);
}