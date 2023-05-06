package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@JsonPropertyOrder
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long owner) {
        log.info("Добавление вещи для бронирования пользователем userId={}", owner);
        return itemClient.createItem(itemDto, owner);
    }

    @PatchMapping("/{idItem}")
    public ResponseEntity<Object> patchItem(@RequestBody ItemDto itemDto, @PathVariable("idItem") Long itemId,
                                            @RequestHeader(value = "X-Sharer-User-Id") Long owner) {
        log.info("Обновление вещи для бронирования - itemId = {}, пользователем - userId={}", itemId, owner);
        return itemClient.patchItem(itemId, itemDto, owner);
    }

    @GetMapping("/{idItem}")
    public ResponseEntity<Object> getItem(@PathVariable("idItem") Long itemId,
                                          @RequestHeader(value = "X-Sharer-User-Id") Long owner) {
        log.info("Получение вещи для бронирования - itemId = {}, пользователем - userId={}", itemId, owner);
        return itemClient.getItem(itemId, owner);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemByText(@RequestParam String text,
                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Поиск вещи по ключевому слову - {}", text);
        return itemClient.searchItemByText(text, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getItemByUserId(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получение вещей пользователя - userId = {}", userId);
        return itemClient.getItemByUserId(userId, from, size);
    }


    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDtoRequest commentDto,
                                                @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId) {
        log.info("Создание комментария пользователем - userId = {}, для вещи - itemId = {}", userId, itemId);
        return itemClient.createComment(commentDto, itemId, userId);
    }
}
