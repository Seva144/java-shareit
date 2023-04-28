package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.createItemRequest(userId, itemRequestDto);
    }

    @GetMapping()
    public List<ItemRequestDto> getAllItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllItemRequestsForUser(userId);
    }

    @GetMapping("/{id}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        return itemRequestService.getItemRequestById(userId, id);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemRequestService.getAllItemRequestsExceptByUser(userId, from, size);
    }


}
