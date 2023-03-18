package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ErrorResponse;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
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
    public ItemDto createItem(@Valid @RequestBody ItemDto item, @RequestHeader(value = "X-Sharer-User-Id") String userId)
            throws NotFoundException {
        int owner = Integer.parseInt(userId);
        return itemService.createItem(item, owner);
    }

    @PatchMapping("/{idItem}")
    public ItemDto patchItem(@PathVariable("idItem") Integer idItem, @RequestBody Map<String, Object> fields,
                             @RequestHeader(value = "X-Sharer-User-Id") String userId)
            throws ValidationException, NotFoundException {
        if (idItem == null) throw new ValidationException("User not found");
        int owner = Integer.parseInt(userId);
        return itemService.patchItem(idItem, fields, owner);
    }

    @GetMapping("/{idItem}")
    public ItemDto getItem(@PathVariable("idItem") Integer idItem) {
        return itemService.getItem(idItem);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemByText(@RequestParam Optional<String> text) throws NotFoundException {
        if (text.isEmpty()) throw new NotFoundException();
        else return itemService.searchItemsByText(text.get());
    }

    @GetMapping
    public List<ItemDto> searchItemByUserId(@RequestHeader(value = "X-Sharer-User-Id") String userId) {
        int owner = Integer.parseInt(userId);
        return itemService.searchItemByUserId(owner);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final ValidationException e) {
        return new ErrorResponse(
                "Validation error", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFound(final NotFoundException e) {
        return new ErrorResponse(
                "User not found", e.getMessage()
        );
    }
}
