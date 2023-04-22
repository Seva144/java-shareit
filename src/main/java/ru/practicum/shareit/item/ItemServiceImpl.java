package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotRightsException;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<ItemDto> searchItemsByText(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<ItemDto> itemsDto = new ArrayList<>();
            itemRepository.findAll()
                    .stream()
                    .filter(item ->
                            item.getName().toLowerCase().contains(text.toLowerCase()) |
                                    item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(item -> String.valueOf(item.isAvailable()).equals("true"))
                    .forEach(item -> itemsDto.add(ItemMapper.mapToDto(item)));
            return itemsDto;
        }
    }


    @Override
    public ItemDto createItem(Long owner, ItemDto itemDto) {
        validUser(owner);
        Item item = itemRepository.save(ItemMapper.mapToModel(owner, itemDto));
        return getItem(item.getId(), owner);
    }

    @Override
    public ItemDto getItem(Long itemId, Long userId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Такой позиции не существует"));
        ItemDto itemDto = ItemMapper.mapToDto(itemRepository.getReferenceById(itemId));
        List<CommentDtoResponse> comments = getCommentsByItemId(itemId);
        if (comments != null) itemDto.setComments(comments);
        if (userId == itemRepository.getReferenceById(itemId).getOwner()) {
            Booking lastBooking = bookingRepository
                    .getFirstByItemIdAndStartBeforeOrderByStartDesc(itemId, LocalDateTime.now());
            Booking nextBooking = bookingRepository
                    .findTopByItemIdAndStartAfterAndEndAfterOrderByStartAsc(itemId, LocalDateTime.now(), LocalDateTime.now());

            if (lastBooking != null)
                itemDto.setLastBooking(BookingMapper.mapToShort(lastBooking));
            if (nextBooking != null && nextBooking.getStatus() != Status.REJECTED)
                itemDto.setNextBooking(BookingMapper.mapToShort(nextBooking));

        }
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemByUserId(Long id) {
        return itemRepository.findAllByOwnerOrderByIdAsc(id)
                .stream()
                .map(item -> {
                    ItemDto itemDto = ItemMapper.mapToDto(item);
                    Booking lastBooking = bookingRepository
                            .getFirstByItemIdAndStartBeforeOrderByStartDesc(item.getId(), LocalDateTime.now());
                    Booking nextBooking = bookingRepository
                            .findTopByItemIdAndStartAfterAndEndAfterOrderByStartAsc(item.getId(), LocalDateTime.now(), LocalDateTime.now());
                    List<CommentDtoResponse> comments = getCommentsByItemId(item.getId());
                    if (lastBooking != null)
                        itemDto.setLastBooking(BookingMapper.mapToShort(lastBooking));
                    if (nextBooking != null && nextBooking.getStatus() != Status.REJECTED)
                        itemDto.setNextBooking(BookingMapper.mapToShort(nextBooking));
                    if (comments != null) itemDto.setComments(comments);
                    return itemDto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public ItemDto patchItem(Long idItem, ItemDto itemDto, Long idUser) {
        validUser(idUser);
        validOwner(idItem, idUser);

        Item item = itemRepository.getReferenceById(idItem);

        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());

        itemRepository.save(item);

        return getItem(idItem, idUser);
    }

    @Override
    public CommentDtoResponse createComment(CommentDtoRequest commentDto, Long itemId, Long userId) {
        validUser(userId);
        validBooking(itemId, userId);
        Item item = itemRepository.getReferenceById(itemId);
        User user = userRepository.getReferenceById(userId);
        Comment comment = commentRepository.save(CommentMapper.toCommentModel(commentDto, item, user));
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDtoResponse> getCommentsByItemId(Long itemId) {
        return commentRepository.findAllByItemIdOrderByCreateTimeDesc(itemId)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public void validUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public void validBooking(Long itemId, Long userId) {
        Booking booking = bookingRepository
                .findFirstByItemIdAndUserIdAndStatusAndEndIsBefore(itemId, userId, Status.APPROVED, LocalDateTime.now());
        if (booking == null) throw new NotRightsException("Бронирование данным пользователем не найдено");
    }

    public void validOwner(Long itemId, Long userId) {
        if (itemRepository.getReferenceById(itemId).getOwner() != userId)
            throw new NotFoundException("Отказано в доступе");
    }
}
