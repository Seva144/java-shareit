package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotRightsException;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;


import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    ItemDto itemDto = new ItemDto();
    UserDto userDto1 = new UserDto();
    UserDto userDto2 = new UserDto();
    BookingDtoRequest bookingDtoRequest = new BookingDtoRequest();
    CommentDtoRequest commentDtoRequest = new CommentDtoRequest();

    @BeforeEach
    void setUp() {
        userDto1.setName("name1");
        userDto1.setEmail("user1@mail.ru");
        userDto1 = userService.createUser(userDto1);
        userDto2.setName("name2");
        userDto2.setEmail("user2@mail.ru");
        userDto2 = userService.createUser(userDto2);
        bookingDtoRequest.setStart(LocalDateTime.now().minusHours(3));
        bookingDtoRequest.setEnd(LocalDateTime.now().minusHours(2));
        bookingDtoRequest.setUserId(userDto2.getId());
    }

    @Test
    void createItem() {
        itemDto.setName("item1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        itemDto = itemService.createItem(userDto1.getId(), itemDto);

        Query query = em.createNativeQuery("Select * from item where item.id=?", Item.class);
        query.setParameter(1, itemDto.getId());
        Item item = (Item) query.getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getId(), equalTo(itemDto.getId()));
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.isAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(item.getOwner(), equalTo(userDto1.getId()));

    }

    @Test
    void patchItem() {
        itemDto.setName("item1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        itemDto = itemService.createItem(userDto1.getId(), itemDto);

        ItemDto itemDtoUpdate = new ItemDto();
        itemDtoUpdate.setName("item1_update");
        itemDtoUpdate.setDescription("description1_update");
        itemDtoUpdate.setAvailable(false);

        itemService.patchItem(itemDto.getId(), itemDtoUpdate, userDto1.getId());

        Query query = em.createNativeQuery("Select * from item where item.id=?", Item.class);
        query.setParameter(1, itemDto.getId());
        Item item = (Item) query.getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getId(), equalTo(itemDto.getId()));
        assertThat(item.getName(), equalTo(itemDtoUpdate.getName()));
        assertThat(item.getDescription(), equalTo(itemDtoUpdate.getDescription()));
        assertThat(item.isAvailable(), equalTo(itemDtoUpdate.getAvailable()));
        assertThat(item.getOwner(), equalTo(userDto1.getId()));
    }

    @Test
    void getItem() {
        itemDto.setName("item1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        itemDto = itemService.createItem(userDto1.getId(), itemDto);
        itemDto = itemService.getItem(itemDto.getId(), userDto1.getId());

        Query query = em.createNativeQuery("Select * from item where item.id=?", Item.class);
        query.setParameter(1, itemDto.getId());
        Item item = (Item) query.getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getId(), equalTo(itemDto.getId()));
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.isAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(item.getOwner(), equalTo(userDto1.getId()));
    }

    @Test
    void getItemByUserId() {
        itemDto.setName("item1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        itemService.createItem(userDto1.getId(), itemDto);
        List<ItemDto> items = itemService.getItemByUserId(userDto1.getId(), 1, 10);

        Query query = em.createNativeQuery("Select * from item where item.owner=?", Item.class);
        query.setParameter(1, userDto1.getId());
        List<Item> item = query.getResultList();

        List<ItemDto> itemDto = item.stream().map(ItemMapper::mapToDto).collect(Collectors.toList());

        assertNotNull(itemDto);
        assertEquals(itemDto.size(), 1);
        assertThat(itemDto.get(0).getId(), equalTo(items.get(0).getId()));
        assertThat(itemDto.get(0).getDescription(), equalTo(items.get(0).getDescription()));
        assertThat(itemDto.get(0).getName(), equalTo(items.get(0).getName()));
        assertThat(itemDto.get(0).getAvailable(), equalTo(items.get(0).getAvailable()));
        assertThat(itemDto.get(0).getRequestId(), equalTo(items.get(0).getRequestId()));

    }

    @Test
    void searchItemsByText() {
        itemDto.setName("item1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        itemService.createItem(userDto1.getId(), itemDto);
        List<ItemDto> items = itemService.searchItemsByText("desc", 1, 10);

        Query query = em.createNativeQuery("select * from item " +
                "where lower(item.name) like lower(concat('%', ?1, '%')) " +
                " or lower(item.description) like lower(concat('%', ?1, '%'))", Item.class);
        query.setParameter(1, "desc");
        List<Item> item = query.getResultList();

        List<ItemDto> itemDto = item.stream().map(ItemMapper::mapToDto).collect(Collectors.toList());

        assertNotNull(itemDto);
        assertEquals(itemDto.size(), 1);
        assertThat(itemDto.get(0).getId(), equalTo(items.get(0).getId()));
        assertThat(itemDto.get(0).getDescription(), equalTo(items.get(0).getDescription()));
        assertThat(itemDto.get(0).getName(), equalTo(items.get(0).getName()));
        assertThat(itemDto.get(0).getAvailable(), equalTo(items.get(0).getAvailable()));
        assertThat(itemDto.get(0).getRequestId(), equalTo(items.get(0).getRequestId()));

    }

    @Test
    void createComment() {
        itemDto.setName("item1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        itemDto = itemService.createItem(userDto1.getId(), itemDto);

        bookingDtoRequest.setItemId(itemDto.getId());
        BookingDtoResponse bookingDtoResponse = bookingService.createBooking(bookingDtoRequest, userDto2.getId());
        bookingService.patchBooking(bookingDtoResponse.getId(), true, userDto1.getId());
        commentDtoRequest.setText("comment1");

        CommentDtoResponse commentResponse = itemService.createComment(commentDtoRequest, itemDto.getId(), userDto2.getId());

        Query query = em.createNativeQuery("select * from comments where comments.id=?", Comment.class);
        query.setParameter(1, commentResponse.getId());

        Comment comment = (Comment) query.getSingleResult();
        CommentDtoResponse commentDto = CommentMapper.toCommentDto(comment);

        assertThat(commentDto.getId(), equalTo(commentResponse.getId()));
        assertThat(commentDto.getText(), equalTo(commentResponse.getText()));
        assertThat(commentDto.getAuthorName(), equalTo(commentResponse.getAuthorName()));
    }

    @Test
    void getCommentsByItemId() {
        itemDto.setName("item1");
        itemDto.setDescription("description1");
        itemDto.setAvailable(true);
        itemDto = itemService.createItem(userDto1.getId(), itemDto);

        bookingDtoRequest.setItemId(itemDto.getId());
        BookingDtoResponse bookingDtoResponse = bookingService.createBooking(bookingDtoRequest, userDto2.getId());
        bookingService.patchBooking(bookingDtoResponse.getId(), true, userDto1.getId());
        commentDtoRequest.setText("comment1");

        itemService.createComment(commentDtoRequest, itemDto.getId(), userDto2.getId());
        List<CommentDtoResponse> commentResponse = itemService.getCommentsByItemId(itemDto.getId());

        Query query = em.createNativeQuery("select * from comments where comments.item_id=?", Comment.class);
        query.setParameter(1, itemDto.getId());
        List<Comment> comments = query.getResultList();
        List<CommentDtoResponse> commentsDto = comments
                .stream()
                .map(CommentMapper::toCommentDto).collect(Collectors.toList());

        assertNotNull(commentsDto);
        assertEquals(commentsDto.size(), 1);
        assertThat(commentsDto.get(0).getId(), equalTo(commentResponse.get(0).getId()));
        assertThat(commentsDto.get(0).getText(), equalTo(commentResponse.get(0).getText()));
        assertThat(commentsDto.get(0).getAuthorName(), equalTo(commentResponse.get(0).getAuthorName()));

    }

    @Test
    void validUser() {
        assertThrows(NotFoundException.class, () ->
                itemService.createItem(99L, itemDto));
    }

    @Test
    void validBooking() {
        assertThrows(NotRightsException.class, ()->
                itemService.createComment(commentDtoRequest, 99L, userDto1.getId()));
    }

    @Test
    void validOwner() {
        assertThrows(EntityNotFoundException.class, () ->
                itemService.patchItem(99L, itemDto, userDto1.getId()));
    }
}