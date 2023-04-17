package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShort;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapperStruct {

    ItemMapperStruct INSTANCE = Mappers.getMapper(ItemMapperStruct.class);

    @Mapping(source = "owner", target = "owner")
    Item mapToModel(Long owner, ItemDto itemDto);

    ItemDto mapToDto(Item item);

    ItemShort mapToShort(Item item);
}
