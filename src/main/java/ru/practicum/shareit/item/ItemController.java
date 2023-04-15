package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto item, @RequestHeader(value = "X-Sharer-User-Id") Long owner) {
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
    public List<ItemDto> getItemByText(@RequestParam String text) {
        return itemService.searchItemsByText(text);
    }

    @GetMapping
    public List<ItemDto> getItemByUserId(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemService.getItemByUserId(userId);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse createComment(@Valid @RequestBody CommentDtoRequest commentDto,
                                            @RequestHeader(value = "X-Sharer-User-Id") Long userId,
                                            @PathVariable Long itemId) {
        return itemService.createComment(commentDto, itemId, userId);
    }


}
