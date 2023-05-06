package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotRightsException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Eager
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = validUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.mapToModel(itemRequestDto, user);
        return ItemRequestMapper.mapToDto(itemRequestRepository.save(itemRequest), null);
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId, Long id) {
        User user = validUser(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        List<ItemDto> items = itemRepository
                .getAllByRequestId(id)
                .stream()
                .map(ItemMapper::mapToDto)
                .collect(Collectors.toList());
        return ItemRequestMapper.mapToDto(itemRequest, items);
    }

    @Override
    public List<ItemRequestDto> getAllItemRequestsExceptByUser(Long userId, int from, int size) {
        validPage(from, size);
        return itemRequestRepository
                .findAllByRequestor_IdNot(userId,
                        PageRequest.of(from / size, size, Sort.by("created").descending()))
                .stream()
                .map(itemRequest -> {
                    List<ItemDto> items = itemRepository
                            .getAllByRequestId(itemRequest.getId())
                            .stream()
                            .map(ItemMapper::mapToDto)
                            .collect(Collectors.toList());
                    return ItemRequestMapper.mapToDto(itemRequest, items);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllItemRequestsForUser(Long userId) {
        User user = validUser(userId);
        return itemRequestRepository.findAllByRequestor_IdOrderByCreatedAsc(userId)
                .stream()
                .map(itemRequest -> {
                    List<ItemDto> items = itemRepository
                            .getAllByRequestId(itemRequest.getId())
                            .stream()
                            .map(ItemMapper::mapToDto)
                            .collect(Collectors.toList());
                    return ItemRequestMapper.mapToDto(itemRequest, items);
                })
                .collect(Collectors.toList());
    }

    public User validUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public void validPage(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new NotRightsException("Некорректные параметры вывода");
        }
    }
}
