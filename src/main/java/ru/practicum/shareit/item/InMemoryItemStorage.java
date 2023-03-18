package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private final HashMap<Integer, Item> items = new HashMap<>();
    private static int id;

    public int generateId() {
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
    public Item getItem(int idItem) {
        if (items.containsKey(id)) {
            return items.get(id);
        } else {
            throw new RuntimeException();
        }
    }
}
