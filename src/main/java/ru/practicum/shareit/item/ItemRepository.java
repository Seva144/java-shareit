package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    List<Item> getAllItems();

    Item createItem(Item item);

    Item getItem(long idItem);

}
