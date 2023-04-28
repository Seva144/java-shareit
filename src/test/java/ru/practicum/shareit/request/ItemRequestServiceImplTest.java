package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    ItemDto itemDto = new ItemDto();
    UserDto userDto1 = new UserDto();
    UserDto userDto2 = new UserDto();
    ItemRequestDto itemRequestDto = new ItemRequestDto();

    @BeforeEach
    void setUp() {
        userDto1.setName("name1");
        userDto1.setEmail("user1@mail.ru");
        userDto1 = userService.createUser(userDto1);
        userDto2.setName("name2");
        userDto2.setEmail("user2@mail.ru");
        userDto2 = userService.createUser(userDto2);
        itemDto.setName("item1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        itemDto = itemService.createItem(userDto1.getId(), itemDto);
        itemRequestDto.setDescription("descriptionRequest");
        itemRequestDto.setCreated(LocalDateTime.now());
    }

    @Test
    void createItemRequest() {
        itemRequestDto = itemRequestService.createItemRequest(userDto2.getId(), itemRequestDto);

        Query query = em.createNativeQuery("select * from requests where requests.id=?", ItemRequest.class);
        query.setParameter(1, itemRequestDto.getId());
        ItemRequest itemRequest = (ItemRequest) query.getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getId(), equalTo(itemRequestDto.getId()));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
    }

    @Test
    void getItemRequestById() {
        itemRequestDto = itemRequestService.createItemRequest(userDto2.getId(), itemRequestDto);
        itemRequestDto = itemRequestService.getItemRequestById(userDto2.getId(), itemRequestDto.getId());

        Query query = em.createNativeQuery("select * from requests where requests.id=?1", ItemRequest.class);
        query.setParameter(1, itemRequestDto.getId());
        ItemRequest itemRequest = (ItemRequest) query.getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getId(), equalTo(itemRequestDto.getId()));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestDto.getDescription()));
    }

    @Test
    void getAllItemRequestsExceptByUser() {
        itemRequestService.createItemRequest(userDto2.getId(), itemRequestDto);
        List<ItemRequestDto> itemRequestsDto = itemRequestService
                .getAllItemRequestsExceptByUser(userDto1.getId(), 1, 10);

        Query query = em.createNativeQuery("select * from requests where not requests.requestor_id=?1", ItemRequest.class);
        query.setParameter(1, userDto1.getId());
        List<ItemRequest> itemRequest = query.getResultList();

        assertNotNull(itemRequest);
        assertEquals(itemRequest.size(), 1);
        assertThat(itemRequest.get(0).getId(), equalTo(itemRequestsDto.get(0).getId()));
        assertThat(itemRequest.get(0).getDescription(), equalTo(itemRequestsDto.get(0).getDescription()));

    }

    @Test
    void getAllItemRequestsForUser() {
        itemRequestService.createItemRequest(userDto2.getId(), itemRequestDto);
        List<ItemRequestDto> itemRequestsDto = itemRequestService
                .getAllItemRequestsForUser(userDto2.getId());

        Query query = em.createNativeQuery("select * from requests where requests.requestor_id=?1", ItemRequest.class);
        query.setParameter(1, userDto2.getId());
        List<ItemRequest> itemRequest = query.getResultList();

        assertNotNull(itemRequest);
        assertEquals(itemRequest.size(), 1);
        assertThat(itemRequest.get(0).getId(), equalTo(itemRequestsDto.get(0).getId()));
        assertThat(itemRequest.get(0).getDescription(), equalTo(itemRequestsDto.get(0).getDescription()));
    }

    @Test
    void validUser() {
    }

    @Test
    void validPage() {
    }
}