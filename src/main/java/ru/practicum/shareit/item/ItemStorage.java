package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemStorage {

    List<Item> getAllItems();

    Item createItem(Item item);

    Item getItem(int idItem);

}
