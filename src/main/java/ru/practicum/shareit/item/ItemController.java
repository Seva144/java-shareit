package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto item, @RequestHeader(value = "X-Sharer-User-Id") Long owner)
            throws NotFoundException {
        return itemService.createItem(owner, item);
    }

    @PatchMapping("/{idItem}")
    public ItemDto patchItem(@PathVariable("idItem") Long idItem, @RequestBody Map<String, Object> fields,
                             @RequestHeader(value = "X-Sharer-User-Id") Long owner)
            throws NotFoundException {
        return itemService.patchItem(idItem, fields, owner);
    }

    @GetMapping("/{idItem}")
    public ItemDto getItem(@PathVariable("idItem") Integer idItem) {
        return itemService.getItem(idItem);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam Optional<String> text) {
        return itemService.searchItemsByText(text.get());
    }

    @GetMapping
    public List<ItemDto> searchItemByUserId(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemService.searchItemByUserId(userId);
    }
}
