package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ItemDaoImpl implements ItemDao {

    private final HashMap<Long, Item> items = new HashMap<>();
    private static long id;

    public long generateId() {
        return ++id;
    }

    @Override
    public List<Item> getAllItems() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item createItem(Item item) {
        item.setId(generateId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItem(long idItem) {
        return items.get(id);
    }
}
