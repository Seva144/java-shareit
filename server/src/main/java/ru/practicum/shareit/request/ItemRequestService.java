package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getAllItemRequestsExceptByUser(Long userId, int from, int size);

    ItemRequestDto getItemRequestById(Long userId, Long id);

    List<ItemRequestDto> getAllItemRequestsForUser(Long userId);

}
