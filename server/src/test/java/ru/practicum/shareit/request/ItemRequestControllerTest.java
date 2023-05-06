package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto = new UserDto(0L, "user1", "user1@mail.ru");
    private final ItemDto itemDto = new ItemDto(0L, "item1", "description1",
            true, 0L, null, null, null);
    private final List<ItemDto> items = new ArrayList<>();
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(0L, "request",
            LocalDateTime.now(), null);

    @BeforeEach
    void setUp() {
        items.add(itemDto);
        itemRequestDto.setItems(items);
    }

    @SneakyThrows
    @Test
    void create() {
        when(itemRequestService.createItemRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", userDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(itemRequestDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(itemRequestDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.is(itemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.is(itemDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.is(itemDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].available", Matchers.is(itemDto.getAvailable()), Boolean.class));

        verify(itemRequestService).createItemRequest(anyLong(), any(ItemRequestDto.class));
    }

    @SneakyThrows
    @Test
    void getAllItemRequest() {
        when(itemRequestService.getAllItemRequestsForUser(anyLong()))
                .thenReturn(Collections.singletonList(itemRequestDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(itemRequestDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is(itemRequestDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].id", Matchers.is(itemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].name", Matchers.is(itemDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].description", Matchers.is(itemDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].available", Matchers.is(itemDto.getAvailable()), Boolean.class));

        verify(itemRequestService).getAllItemRequestsForUser(anyLong());
    }

    @SneakyThrows
    @Test
    void getItemRequestById() {
        when(itemRequestService.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);
        mvc.perform(MockMvcRequestBuilders.get("/requests/{id}", itemRequestDto.getId())
                        .header("X-Sharer-User-Id", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(itemRequestDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is(itemRequestDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.is(itemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.is(itemDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.is(itemDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].available", Matchers.is(itemDto.getAvailable()), Boolean.class));

        verify(itemRequestService).getItemRequestById(anyLong(), anyLong());

    }

    @SneakyThrows
    @Test
    void getAllRequestsForUser() {
        when(itemRequestService.getAllItemRequestsExceptByUser(anyLong(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(itemRequestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(itemRequestDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", Matchers.is(itemRequestDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].id", Matchers.is(itemDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].name", Matchers.is(itemDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].description", Matchers.is(itemDto.getDescription()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].items[0].available", Matchers.is(itemDto.getAvailable()), Boolean.class));

        verify(itemRequestService).getAllItemRequestsExceptByUser(anyLong(), anyInt(), anyInt());
    }

}