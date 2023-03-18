package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    List<Item> getAllItems();

    Item createItem(Item item);

    Item getItem(int idItem);

}
