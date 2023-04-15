package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
public interface ItemService {

    List<ItemDto> searchItemsByText(String text);

    ItemDto createItem(Long owner, ItemDto itemDto);

    ItemDto getItem(Long idItem, Long owner);

    ItemDto patchItem(Long idItem, ItemDto itemDto, Long owner);

    List<ItemDto> getItemByUserId(Long id);

    CommentDtoResponse createComment(CommentDtoRequest commentDto, Long itemId, Long userId);

    List<CommentDtoResponse> getCommentsByItemId(Long itemId);

}
