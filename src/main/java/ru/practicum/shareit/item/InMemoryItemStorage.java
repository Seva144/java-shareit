package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

//    @Override
//    public Item patchItem(int idItem, Map<String, Object> fields) {
//        if(items.containsKey(id)){
//            items.get(id).setName(item.getName());
//            items.get(id).setDescription(item.getName());
//            items.get(id).setAvailable(item.isAvailable());
//            return items.get(id);
//        } else {
//            throw new RuntimeException();
//        }
//    }

    @Override
    public Item getItem(int idItem){
        if(items.containsKey(id)){
            return items.get(id);
        } else {
            throw new RuntimeException();
        }
    }




}
