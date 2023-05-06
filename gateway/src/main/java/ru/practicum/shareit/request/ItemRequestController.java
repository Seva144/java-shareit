package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Создание запроса пользователем userId={}", userId);
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получить список всех запросов пользователя userId={}", userId);
        return itemRequestClient.getAllItemRequestsForUser(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        log.info("Получить запрос id = {}, пользователя userId={}", id, userId);
        return itemRequestClient.getItemRequestById(userId, id);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequestsExceptByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получить список всех запросов за исключением пользователя userId={}", userId);
        return itemRequestClient.getAllItemRequestsExceptByUser(userId, from, size);
    }


}
