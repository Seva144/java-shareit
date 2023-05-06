package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.user.dto.UserShort;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @MockBean
    BookingService bookingService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;


    private final ItemDto itemDto = new ItemDto(0L, "item1", "description1", true,
            0L, null, null, null);
    private final UserShort userShort = new UserShort(0L);
    private final ItemDtoShort itemDtoShort = new ItemDtoShort(0L, "itemDtoShort");

    private final BookingDtoResponse bookingDtoResponse = new BookingDtoResponse(0L, LocalDateTime.now().plusHours(2),
            LocalDateTime.now().plusHours(3), Status.valueOf("WAITING"), userShort, itemDtoShort);

    @SneakyThrows
    @Test
    void createBooking() {
        when(bookingService.createBooking(any(BookingDtoRequest.class), anyLong()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userShort.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDtoResponse))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingDtoResponse.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(String.valueOf(bookingDtoResponse.getStatus())), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.id",
                        Matchers.is(bookingDtoResponse.getBooker().getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.id",
                        Matchers.is(bookingDtoResponse.getItem().getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name",
                        Matchers.is(bookingDtoResponse.getItem().getName()), String.class));

        verify(bookingService).createBooking(any(BookingDtoRequest.class), anyLong());
    }

    @SneakyThrows
    @Test
    void patchBooking() {
        when(bookingService.patchBooking(anyLong(), anyBoolean(), anyLong()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(MockMvcRequestBuilders.patch("/bookings/" + bookingDtoResponse.getId())
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userShort.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingDtoResponse.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(String.valueOf(bookingDtoResponse.getStatus())), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.id",
                        Matchers.is(bookingDtoResponse.getBooker().getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.id",
                        Matchers.is(bookingDtoResponse.getItem().getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name",
                        Matchers.is(bookingDtoResponse.getItem().getName()), String.class));

        verify(bookingService).patchBooking(anyLong(), anyBoolean(), anyLong());
    }

    @SneakyThrows
    @Test
    void getBooking() {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(MockMvcRequestBuilders.get("/bookings/" + bookingDtoResponse.getId())
                        .header("X-Sharer-User-Id", userShort.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(bookingDtoResponse.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is(String.valueOf(bookingDtoResponse.getStatus())), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.id",
                        Matchers.is(bookingDtoResponse.getBooker().getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.id",
                        Matchers.is(bookingDtoResponse.getItem().getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name",
                        Matchers.is(bookingDtoResponse.getItem().getName()), String.class));

        verify(bookingService).getBooking(anyLong(), anyLong());
    }

    @SneakyThrows
    @Test
    void getAllUserBooking() {
        when(bookingService.getAllUserBooking(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(bookingDtoResponse));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userShort.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(bookingDtoResponse.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(String.valueOf(bookingDtoResponse.getStatus())),
                        String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.id",
                        Matchers.is(bookingDtoResponse.getBooker().getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.id",
                        Matchers.is(bookingDtoResponse.getItem().getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name",
                        Matchers.is(bookingDtoResponse.getItem().getName()), String.class));

        verify(bookingService).getAllUserBooking(anyLong(), any(), anyInt(), anyInt());
    }

    @SneakyThrows
    @Test
    void getAllOwnerBooking() {
        when(bookingService.getAllOwnerBooking(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(bookingDtoResponse));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userShort.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(bookingDtoResponse.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is(String.valueOf(bookingDtoResponse.getStatus())), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.id",
                        Matchers.is(bookingDtoResponse.getBooker().getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.id",
                        Matchers.is(bookingDtoResponse.getItem().getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name",
                        Matchers.is(bookingDtoResponse.getItem().getName()), String.class));

        verify(bookingService).getAllOwnerBooking(anyLong(), any(), anyInt(), anyInt());
    }
}