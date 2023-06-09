package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toCommentModel(CommentDtoRequest commentDto, Item item, User user) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setUser(user);
        comment.setCreateTime(LocalDateTime.now());
        return comment;
    }

    public static CommentDtoResponse toCommentDto(Comment comment) {
        return CommentDtoResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getUser().getName())
                .created(comment.getCreateTime())
                .build();
    }
}
