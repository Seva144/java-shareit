package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemService(ItemRepository itemStorage, UserRepository userStorage) {
        this.itemRepository = itemStorage;
        this.userRepository = userStorage;
    }

    public List<ItemDto> searchItemsByText(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<ItemDto> itemsDto = new ArrayList<>();
            itemRepository.getAllItems()
                    .stream()
                    .filter(item ->
                            item.getName().toLowerCase().contains(text.toLowerCase()) |
                                    item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(item -> String.valueOf(item.isAvailable()).equals("true"))
                    .forEach(item -> itemsDto.add(ItemMapper.mapToDto(item)));
            return itemsDto;
        }
    }

    public List<ItemDto> searchItemByUserId(long id) {
        List<ItemDto> itemsDto = new ArrayList<>();
        itemRepository.getAllItems()
                .stream()
                .filter(item -> item.getOwner() == id)
                .forEach(item -> itemsDto.add(ItemMapper.mapToDto(item)));
        return itemsDto;
    }

    public ItemDto createItem(long owner, ItemDto itemDto) throws NotFoundException {
        validUser(owner);
        Item item = itemRepository.createItem(ItemMapper.mapToModel(owner, itemDto));
        return getItem(item.getId());
    }

    public ItemDto patchItem(long idItem, Map<String, Object> fields, long idUser) throws NotFoundException {
        validUser(idUser);
        if (itemRepository.getItem(idItem).getOwner() != idUser) throw new NotFoundException();
        Item excitingItem = itemRepository.getItem(idItem);
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Item.class, key);
            Objects.requireNonNull(field).setAccessible(true);
            ReflectionUtils.setField(field, excitingItem, value);
        });
        return getItem(idItem);
    }

    public ItemDto getItem(long idItem) {
        return ItemMapper.mapToDto(itemRepository.getItem(idItem));
    }

    public void validUser(long id) throws NotFoundException {
        if (userRepository.getUser(id) == null) throw new NotFoundException();
    }
}
