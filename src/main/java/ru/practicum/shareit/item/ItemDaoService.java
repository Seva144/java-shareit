package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.mapper.ItemMapperStruct;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ItemDaoService {

    private final ItemDao itemDao;
    private final UserDao userDao;

    public ItemDaoService(ItemDao itemStorage, UserDao userStorage) {
        this.itemDao = itemStorage;
        this.userDao = userStorage;
    }

    public List<ItemDto> searchItemsByText(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<ItemDto> itemsDto = new ArrayList<>();
            itemDao.getAllItems()
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
        return itemDao.getAllItems()
                .stream()
                .filter(item -> item.getOwner() == id)
                .map(ItemMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public ItemDto createItem(long owner, ItemDto itemDto) {
        validUser(owner);
        Item item = itemDao.createItem(ItemMapper.mapToModel(owner, itemDto));
        return getItem(item.getId());
    }

    public ItemDto patchItem(long idItem, ItemDto itemDto, long idUser) {
        validUser(idUser);
        validOwner(idItem, idUser);

        if (itemDto.getName() != null) itemDao.getItem(idItem).setName(itemDto.getName());
        if (itemDto.getAvailable() != null) itemDao.getItem(idItem).setAvailable(itemDto.getAvailable());
        if (itemDto.getDescription() != null) itemDao.getItem(idItem).setDescription(itemDto.getDescription());

        return getItem(idItem);

    }

    public ItemDto getItem(long idItem) {
        return ItemMapper.mapToDto(itemDao.getItem(idItem));
    }

    public void validUser(long id) {
        if (userDao.getUser(id) == null) throw new NotFoundException("Пользователь не найден");
    }

    public void validOwner(long idItem, long idUser) {
        if (itemDao.getItem(idItem).getOwner() != idUser)
            throw new NotFoundException("Отказано в доступе");
    }
}
