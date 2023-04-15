package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

/*    Сообщение для ревьюера: Проблема по по поиску теста не ищет никак запросами, даже напрямую с обращением к БД.
Вроде как проблема с кодировкой UTF-8, так как с тектом латиницей такой проблемы нет
    */


    @Query(value = " select i from item as i " +
            "where lower(i.name) like lower(concat('%', ?1, '%')) " +
            "   or lower(i.description) like lower(concat('%', ?1, '%'))",
            nativeQuery = true)
    List<Item> searchByText(String text);

    List<Item> findAllByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(String description, String name);

    @Query(value = "select i.id from item as i where id=?1",
            nativeQuery = true)
    Long validItem(Long id);

    List<Item> findAllByOwnerOrderByIdAsc(Long id);


}
