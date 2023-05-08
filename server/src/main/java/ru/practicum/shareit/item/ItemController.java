package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.CommentDtoResponse;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto item, @RequestHeader(value = "X-Sharer-User-Id") Long owner) {
        return itemService.createItem(owner, item);
    }

    @PatchMapping("/{idItem}")
    public ItemDto patchItem(@PathVariable("idItem") Long idItem, @RequestBody ItemDto itemDto,
                             @RequestHeader(value = "X-Sharer-User-Id") Long owner) {
        return itemService.patchItem(idItem, itemDto, owner);
    }

    @GetMapping("/{idItem}")
    public ItemDto getItem(@PathVariable("idItem") Long idItem,
                           @RequestHeader(value = "X-Sharer-User-Id") Long owner) {
        return itemService.getItem(idItem, owner);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByText(@RequestParam String text,
                                       @RequestParam(required = false, defaultValue = "0") Integer from,
                                       @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemService.searchItemsByText(text, from, size);
    }

    @GetMapping
    public List<ItemDto> getItemByUserId(@RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemService.getItemByUserId(userId, from, size);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse createComment(@RequestBody CommentDtoRequest commentDto,
                                            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                            @PathVariable Long itemId) {
        return itemService.createComment(commentDto, itemId, userId);
    }


}
