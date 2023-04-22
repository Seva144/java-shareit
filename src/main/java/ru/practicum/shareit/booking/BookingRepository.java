package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserIdAndStatus(Long idUser, Status status);

    List<Booking> findAllByItem_OwnerAndStatus(Long idUser, Status status);

//    lastbooking

    Booking getFirstByItemIdAndStartBeforeOrderByStartDesc(Long idItem, LocalDateTime now);

//    nextbooking

    Booking findTopByItemIdAndStartAfterAndEndAfterOrderByStartAsc(Long itemId, LocalDateTime now, LocalDateTime start);

    List<Booking> findAllByUserIdOrderByStartDesc(Long idUser);

    List<Booking> findAllByUserIdAndStartIsAfterOrderByStartDesc(Long idUser, LocalDateTime now);

    List<Booking> findAllByUserIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long idUser, LocalDateTime nowFirst, LocalDateTime nowSecond);

    List<Booking> findAllByItem_OwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(Long idUser, LocalDateTime nowFirst, LocalDateTime nowSecond);

    List<Booking> findAllByUserIdAndEndIsBeforeOrderByStartDesc(Long idUser, LocalDateTime now);

    List<Booking> findAllByItem_OwnerAndEndIsBeforeOrderByStartDesc(Long idUser, LocalDateTime now);

    List<Booking> findAllByItem_OwnerOrderByStartDesc(Long idUser);

    List<Booking> findAllByItem_OwnerAndStartIsAfterOrderByStartDesc(Long idUser, LocalDateTime now);

    Booking findFirstByItemIdAndUserIdAndStatusAndEndIsBefore(Long itemId, Long userId, Status status, LocalDateTime now);


}
