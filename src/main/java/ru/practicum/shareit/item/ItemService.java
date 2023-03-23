package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return itemRepository.getAllItems()
                .stream()
                .filter(item -> item.getOwner() == id)
                .map(ItemMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public ItemDto createItem(long owner, ItemDto itemDto) throws NotFoundException {
        validUser(owner);
        Item item = itemRepository.createItem(ItemMapper.mapToModel(owner, itemDto));
        return getItem(item.getId());
    }

    public ItemDto patchItem(long idItem, ItemDto itemDto, long idUser) throws NotFoundException {
        validUser(idUser);
        if (itemRepository.getItem(idItem).getOwner() != idUser) throw new NotFoundException();

        if (itemDto.getName() != null) itemRepository.getItem(idItem).setName(itemDto.getName());
        if (itemDto.getAvailable() != null) itemRepository.getItem(idItem).setAvailable(itemDto.getAvailable());
        if (itemDto.getDescription() != null) itemRepository.getItem(idItem).setDescription(itemDto.getDescription());

        return getItem(idItem);

    }

    public ItemDto getItem(long idItem) {
        return ItemMapper.mapToDto(itemRepository.getItem(idItem));
    }

    public void validUser(long id) throws NotFoundException {
        if (userRepository.getUser(id) == null) throw new NotFoundException();
    }
}
