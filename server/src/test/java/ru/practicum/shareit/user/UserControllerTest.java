package ru.practicum.shareit.user;

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
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private final UserDto userDto = new UserDto(1, "user1", "user1@mail.ru");

    @SneakyThrows
    @Test
    void createUser() {
        when(userService.createUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(userDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(userDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is(userDto.getEmail()), String.class));

        verify(userService).createUser(any());
    }

    @SneakyThrows
    @Test
    void patchUser() {
        when(userService.patchUser(anyLong(), any(UserDto.class)))
                .thenReturn(userDto);

        mvc.perform(MockMvcRequestBuilders.patch("/users/" + userDto.getId())
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(userDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(userDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is(userDto.getEmail()), String.class));

        verify(userService).patchUser(anyLong(), any(UserDto.class));
    }

    @SneakyThrows
    @Test
    void getAllUsers() {
        when(userService.getAllUsers())
                .thenReturn(Collections.emptyList());
        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(userService).getAllUsers();
    }

    @SneakyThrows
    @Test
    void getUser() {
        when(userService.getUser(anyLong()))
                .thenReturn(userDto);

        mvc.perform(MockMvcRequestBuilders.get("/users/" + userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(userDto.getId()), Long.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(userDto.getName()), String.class))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Is.is(userDto.getEmail()), String.class));

        verify(userService).getUser(userDto.getId());
    }

    @SneakyThrows
    @Test
    void deleteUser() {
        mvc.perform(MockMvcRequestBuilders.delete("/users/" + userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).deleteUser(userDto.getId());
    }

}