package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

//    lastbooking

    Booking getFirstByItemIdAndStartBeforeOrderByStartDesc(Long idItem, LocalDateTime now);

//    nextbooking

    Booking findTopByItemIdAndStartAfterAndEndAfterOrderByStartAsc(Long itemId, LocalDateTime now, LocalDateTime start);

//    getAllUserBooking

    List<Booking> findAllByUserIdAndStatus(Long idUser, Status status, Pageable pageable);

    List<Booking> findAllByUserIdOrderByStartDesc(Long idUser, Pageable pageable);

    List<Booking> findAllByUserIdAndStartIsAfterOrderByStartDesc(Long idUser, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long idUser, LocalDateTime nowFirst, LocalDateTime nowSecond, Pageable pageable);

    List<Booking> findAllByUserIdAndEndIsBeforeOrderByStartDesc(Long idUser, LocalDateTime now, Pageable pageable);

//    getAllOwnerBooking

    List<Booking> findAllByItem_OwnerAndStatus(Long idUser, Status status, Pageable pageable);

    List<Booking> findAllByItem_OwnerOrderByStartDesc(Long idUser, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(Long idUser, LocalDateTime now, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long idUser, LocalDateTime nowFirst, LocalDateTime nowSecond, Pageable pageable);

    List<Booking> findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(Long idUser, LocalDateTime now, Pageable pageable);

//    validBooking

    Booking findFirstByItemIdAndUserIdAndStatusAndEndIsBefore(Long itemId, Long userId, Status status, LocalDateTime now);


}
