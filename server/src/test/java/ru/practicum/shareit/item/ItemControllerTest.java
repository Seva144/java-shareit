package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
@AutoConfigureMockMvc
class ItemControllerTest {

    @MockBean
    ItemService itemService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto = new UserDto(0L, "user1", "user1@mail.ru");

    private final CommentDtoResponse commentDtoResponse = new CommentDtoResponse(0L, "text1",
            "name1", LocalDateTime.now());

    private final BookingDtoShort bookingDtoShort1 = new BookingDtoShort(0L, 0L);
    private final BookingDtoShort bookingDtoShort2 = new BookingDtoShort(1L, 1L);
    private final List<CommentDtoResponse> comments = new ArrayList<>();


    private final ItemDto itemDto = new ItemDto(0L, "item1", "description1",
            true, 0L, bookingDtoShort1, bookingDtoShort2, null);

    @SneakyThrows
    @Test
    void createItem() {
        when(itemService.createItem(anyLong(), any(ItemDto.class)))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(itemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(itemDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Is.is(itemDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastBooking", Is.is(itemDto.getLastBooking()), BookingDtoShort.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Is.is(itemDto.getAvailable()), Boolean.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestId", Is.is(itemDto.getRequestId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nextBooking", Is.is(itemDto.getNextBooking()), BookingDtoShort.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments", Is.is(itemDto.getComments()), CommentDtoResponse.class));

        verify(itemService).createItem(anyLong(), any(ItemDto.class));
    }

    @SneakyThrows
    @Test
    void patchItem() {
        when(itemService.patchItem(anyLong(), any(ItemDto.class), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(MockMvcRequestBuilders.patch("/items/" + itemDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(itemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(itemDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Is.is(itemDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Is.is(itemDto.getAvailable()), Boolean.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestId", Is.is(itemDto.getRequestId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastBooking", Is.is(itemDto.getLastBooking()), BookingDtoShort.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nextBooking", Is.is(itemDto.getNextBooking()), BookingDtoShort.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments", Is.is(itemDto.getComments()), CommentDtoResponse.class));

        verify(itemService).patchItem(anyLong(), any(ItemDto.class), anyLong());
    }

    @SneakyThrows
    @Test
    void getItem() {
        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemDto);
        mvc.perform(MockMvcRequestBuilders.get("/items/" + itemDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", itemDto.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(itemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(itemDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Is.is(itemDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.available", Is.is(itemDto.getAvailable()), Boolean.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.requestId", Is.is(itemDto.getRequestId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastBooking", Is.is(itemDto.getLastBooking()), BookingDtoShort.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nextBooking", Is.is(itemDto.getNextBooking()), BookingDtoShort.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comments", Is.is(itemDto.getComments()), CommentDtoResponse.class));

        verify(itemService).getItem(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void getItemByText() {
        when(itemService.searchItemsByText(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search").param("text", "item1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Is.is(itemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Is.is(itemDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Is.is(itemDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available", Is.is(itemDto.getAvailable()), Boolean.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requestId", Is.is(itemDto.getRequestId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastBooking", Is.is(itemDto.getLastBooking()), BookingDtoShort.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nextBooking", Is.is(itemDto.getNextBooking()), BookingDtoShort.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].comments", Is.is(itemDto.getComments()), CommentDtoResponse.class));

        verify(itemService).searchItemsByText(anyString(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getItemByUserId() {
        when(itemService.getItemByUserId(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Is.is(itemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Is.is(itemDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Is.is(itemDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available", Is.is(itemDto.getAvailable()), Boolean.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].requestId", Is.is(itemDto.getRequestId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastBooking", Is.is(itemDto.getLastBooking()), BookingDtoShort.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nextBooking", Is.is(itemDto.getNextBooking()), BookingDtoShort.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].comments", Is.is(itemDto.getComments()), CommentDtoResponse.class));

        verify(itemService).getItemByUserId(anyLong(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void createComment() {
        when(itemService.createComment(any(CommentDtoRequest.class), anyLong(), anyLong()))
                .thenReturn(commentDtoResponse);

        mvc.perform(MockMvcRequestBuilders.post("/items/{itemId}/comment", itemDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDtoResponse))
                        .header("X-Sharer-User-Id", userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(commentDtoResponse.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text", Is.is(commentDtoResponse.getText()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authorName", Is.is(commentDtoResponse.getAuthorName()), String.class));

        verify(itemService).createComment(any(CommentDtoRequest.class), anyLong(), anyLong());
    }
}