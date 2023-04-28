package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotRightsException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Eager
public class BookingServiceImpl implements BookingService {


    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    public BookingDtoResponse createBooking(BookingDtoRequest bookingDto, Long userId) {
        validBookingToController(bookingDto, userId);
        validBookingFromDB(bookingDto);

        Item item = itemRepository.getReferenceById(bookingDto.getItemId());

        if (item.getOwner() == userId)
            throw new NotFoundException("Вы не можете забронировать свою вещь");

        User user = userRepository.getReferenceById(userId);

        Booking booking = BookingMapper.mapToModel(bookingDto, user, item);
        booking.setStatus(Status.WAITING);
        bookingRepository.save(booking);
        return getBooking(userId, booking.getId());
    }

    @Override
    public BookingDtoResponse patchBooking(Long idBooking, Boolean approved, Long userId) {

        Booking booking = bookingRepository.getReferenceById(idBooking);

        validUserWithBooking(booking, userId);

        if (!booking.getStatus().equals(Status.WAITING))
            throw new NotRightsException("Невозможно поменять статус объекта");

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingRepository.save(booking);
        return getBooking(userId, idBooking);
    }

    @Override
    public BookingDtoResponse getBooking(Long idUser, Long idBooking) {
        bookingRepository.findById(idBooking)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        Booking booking = bookingRepository.getReferenceById(idBooking);
        if (!booking.getUser().getId().equals(idUser)) {
            validUserWithBooking(booking, idUser);
        }
        return (BookingMapper.mapToDto(booking));
    }

    @Override
    public List<BookingDtoResponse> getAllUserBooking(Long idUser, String state, Integer from, Integer size) {
        validUser(idUser);
        validPage(from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        switch (state) {
            case ("ALL"):
                return bookingRepository.findAllByUserIdOrderByStartDesc(idUser, pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            case ("FUTURE"):
                return bookingRepository
                        .findAllByUserIdAndStartIsAfterOrderByStartDesc(idUser, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            case ("WAITING"):
                return bookingRepository
                        .findAllByUserIdAndStatus(idUser, Status.WAITING, pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            case ("CURRENT"):
                return bookingRepository
                        .findAllByUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(idUser, LocalDateTime.now(), LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            case ("PAST"):
                return bookingRepository
                        .findAllByUserIdAndEndIsBeforeOrderByStartDesc(idUser, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            case ("REJECTED"):
                return bookingRepository
                        .findAllByUserIdAndStatus(idUser, Status.REJECTED, pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            default:
                throw new NotRightsException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingDtoResponse> getAllOwnerBooking(Long idUser, String state, Integer from, Integer size) {
        validUser(idUser);
        validPage(from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        switch (state) {
            case ("ALL"):
                return bookingRepository
                        .findAllByItem_OwnerOrderByStartDesc(idUser, pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            case ("FUTURE"):
                return bookingRepository
                        .findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(idUser, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            case ("WAITING"):
                return bookingRepository
                        .findAllByItem_OwnerAndStatus(idUser, Status.WAITING, pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            case ("CURRENT"):
                return bookingRepository
                        .findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(idUser, LocalDateTime.now(), LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            case ("PAST"):
                return bookingRepository
                        .findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(idUser, LocalDateTime.now(), pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            case ("REJECTED"):
                return bookingRepository
                        .findAllByItem_OwnerAndStatus(idUser, Status.REJECTED, pageable)
                        .stream()
                        .map(BookingMapper::mapToDto)
                        .collect(Collectors.toList());
            default:
                throw new NotRightsException("Unknown state: " + state);
        }
    }

    public void validUser(Long idUser) {
        userRepository.findById(idUser)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    public void validUserWithBooking(Booking booking, Long idUser) {
        Long ownerId = booking.getItem().getOwner();
        if (!ownerId.equals(idUser))
            throw new NotFoundException("Ошибка доступа");
    }

    public void validBookingToController(BookingDtoRequest bookingDto, Long userId) {
        validUser(userId);
        itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Такой позиции не существует"));
        ItemDto itemDto = ItemMapper.mapToDto(itemRepository.getReferenceById(bookingDto.getItemId()));
        if (!itemDto.getAvailable()) throw new NotRightsException("Ошибка доступа");

    }

    public void validBookingFromDB(BookingDtoRequest bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) ||
                bookingDto.getStart().isEqual(bookingDto.getEnd()))
            throw new NotRightsException("Некорректные данные");
    }

    public void validPage(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new NotRightsException("Некорректные параметры вывода");
        }
    }

}
