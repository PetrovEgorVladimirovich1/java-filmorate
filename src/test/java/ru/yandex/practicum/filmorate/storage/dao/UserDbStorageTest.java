package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {

    private final JdbcTemplate jdbcTemplate;

    private UserDbStorage userStorage;

    private User user;

    private User userTest;

    @BeforeEach
    void setUp() {
        userStorage = new UserDbStorage(jdbcTemplate);
        user = new User(1L, "user@email.ru", "vanya123", "Ivan Petrov",
                LocalDate.of(1990, 1, 1));
        userStorage.create(user);
    }

    @Test
    void create() {
        userTest = userStorage.getByIdUser(1);
        assertThat(userTest)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void update() {
        userTest = new User(1L, "egich-2011@mail.ru", "Egor", "Egor Petrov",
                LocalDate.of(2000, 9, 19));
        userStorage.update(userTest);
        assertThat(userStorage.getByIdUser(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userTest);
    }

    @Test
    void getUsers() {
        assertEquals(1, userStorage.getUsers().size());
    }

    @Test
    void getByIdUser() {
        assertThat(userStorage.getByIdUser(1))
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void addFriend() {
        userTest = new User(2L, "egich-2011@mail.ru", "Egor", "Egor Petrov",
                LocalDate.of(2000, 9, 19));
        userStorage.create(userTest);
        userStorage.addFriend(user.getId(), userTest.getId());
        assertEquals(1, userStorage.getUserFriends(user.getId()).size());
    }

    @Test
    void deleteFriend() {
        userTest = new User(2L, "egich-2011@mail.ru", "Egor", "Egor Petrov",
                LocalDate.of(2000, 9, 19));
        userStorage.create(userTest);
        userStorage.addFriend(user.getId(), userTest.getId());
        userStorage.deleteFriend(user.getId(), userTest.getId());
        assertEquals(0, userStorage.getUserFriends(user.getId()).size());
    }

    @Test
    void getUserFriends() {
        addFriend();
        userTest = new User(3L, "egich-2011@mail.ru", "Egor", "Egor Petrov",
                LocalDate.of(2000, 9, 19));
        userStorage.create(userTest);
        userStorage.addFriend(user.getId(), userTest.getId());
        assertEquals(2, userStorage.getUserFriends(user.getId()).size());
    }

    @Test
    void getUserFriendsCommonWithOtherUser() {
        getUserFriends();
        userStorage.addFriend(2, 3);
        assertEquals(1, userStorage.getUserFriendsCommonWithOtherUser(1, 2).size());
    }
}