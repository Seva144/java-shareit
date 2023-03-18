package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.exceptions.BabRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Service
@Slf4j
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemService(ItemStorage itemStorage, UserStorage userStorage) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
    }

    public List<ItemDto> searchItemsByText(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<ItemDto> itemsDto = new ArrayList<>();
            itemStorage.getAllItems()
                    .stream()
                    .filter(item ->
                            item.getName().toLowerCase().contains(text.toLowerCase()) |
                                    item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(item -> String.valueOf(item.isAvailable()).equals("true"))
                    .forEach(item -> {
                        itemsDto.add(itemDtoBuild(item));
                    });
            return itemsDto;
        }
    }

    public List<ItemDto> searchItemByUserId(int id) {
        List<ItemDto> itemsDto = new ArrayList<>();
        itemStorage.getAllItems()
                .stream()
                .filter(item -> item.getOwner() == id)
                .forEach(item -> {
                    itemsDto.add(itemDtoBuild(item));
                });
        return itemsDto;
    }

    public ItemDto createItem(ItemDto itemDto, int owner) throws NotFoundException {
        validUser(owner);
        Item item = new Item.Builder()
                .withName(itemDto.getName())
                .withDescription(itemDto.getDescription())
                .withAvailable(itemDto.getAvailable())
                .withOwner(owner)
                .build();
        itemStorage.createItem(item);
        return getItem(item.getId());
    }

    public ItemDto patchItem(int idItem, Map<String, Object> fields, int idUser) throws NotFoundException {
        validUser(idUser);
        if (itemStorage.getItem(idItem).getOwner() != idUser) throw new NotFoundException();
        Item excitingItem = itemStorage.getItem(idItem);
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Item.class, key);
            Objects.requireNonNull(field).setAccessible(true);
            ReflectionUtils.setField(field, excitingItem, value);
        });
        return getItem(idItem);
    }

    public ItemDto getItem(int idItem) {
        return itemDtoBuild(itemStorage.getItem(idItem));
    }

    public void validUser(int id) throws NotFoundException {
        if (userStorage.getUser(id) == null) throw new NotFoundException();
    }

    public ItemDto itemDtoBuild(Item item) {
        return new ItemDto.Builder()
                .withId(item.getId())
                .withName(item.getName())
                .withDescription(item.getDescription())
                .withAvailable(item.isAvailable())
                .build();
    }
}
