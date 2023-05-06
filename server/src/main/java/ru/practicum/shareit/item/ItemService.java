package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.CommentDtoResponse;

import java.util.List;

@Service
public interface ItemService {

    ItemDto createItem(Long owner, ItemDto itemDto);

    ItemDto getItem(Long idItem, Long owner);

    ItemDto patchItem(Long idItem, ItemDto itemDto, Long owner);

    List<ItemDto> searchItemsByText(String text, Integer from, Integer size);

    List<ItemDto> getItemByUserId(Long id, Integer from, Integer size);

    CommentDtoResponse createComment(CommentDtoRequest commentDto, Long itemId, Long userId);

    List<CommentDtoResponse> getCommentsByItemId(Long itemId);

}
