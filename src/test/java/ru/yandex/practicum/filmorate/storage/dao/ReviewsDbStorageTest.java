package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Reviews;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.ReviewsStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewsDbStorageTest {
    @Autowired
    private FilmDbStorage filmDbStorage;
    @Autowired
    private UserDbStorage userDbStorage;
    @Autowired
    private ReviewsStorage reviewsStorage;

    private Film film;
    private Film film1;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        film = new Film(1L, "Крепкий орешек.", "Описание.",
                LocalDate.of(1988, Month.JULY, 12), 133, new Mpa(4, "R"));
        film1 = new Film(2L, "Крепкий орешек.", "Описание.",
                LocalDate.of(1981, Month.JULY, 14), 121, new Mpa(4, "R"));
        filmDbStorage.create(film);
        filmDbStorage.create(film1);
        user1 = new User(1L, "csddsc@mail.ru", "aaaaa", "aaaa", LocalDate.now());
        user2 = new User(2L, "csddsc@mail.ru", "bbbb", "bbbb", LocalDate.now());
        user3 = new User(3L, "csddsc@mail.ru", "ccccc", "ccccc", LocalDate.now());
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);
    }


    @Test
    public void createAndGet() {
        Reviews reviews = new Reviews("bbuij", true, user2.getId(), film1.getId());
        reviewsStorage.create(reviews);
        Reviews reviews1 = reviewsStorage.get(reviews.getReviewId());
        Assertions.assertEquals(reviews, reviews1);
        System.out.println(reviews);
    }


    @Test
    public void update() {
        Reviews reviews = new Reviews("bbuij", true, user1.getId(), film.getId());
        reviewsStorage.create(reviews);
        Reviews reviews1 = reviewsStorage.get(reviews.getReviewId());
        Assertions.assertEquals(reviews, reviews1);
        reviews1.setContent("новый контент");
        Reviews reviews2 = reviewsStorage.update(reviews1);
        Reviews reviews3 = reviewsStorage.get(1L);
    }


    @Test
    public void remove() {
        Reviews reviews = new Reviews("bbuij", true, user1.getId(), film.getId());
        reviewsStorage.create(reviews);
        Reviews reviews1 = reviewsStorage.get(reviews.getReviewId());
        Assertions.assertEquals(reviews, reviews1);
        reviewsStorage.remove(reviews.getReviewId());
        Assertions.assertNull(reviewsStorage.get(1L));

    }


    @Test
    public void getReviewsFilm() {
        Reviews reviews = new Reviews("bbuij", true, user1.getId(), film.getId());
        Reviews reviews1 = new Reviews("bbuij", true, user1.getId(), film.getId());
        Reviews reviews2 = new Reviews("bbuij", true, user1.getId(), film1.getId());
        reviewsStorage.create(reviews);
        reviewsStorage.create(reviews1);
        reviewsStorage.create(reviews2);
        List<Reviews> reviewsList = reviewsStorage.getReviewsFilm(null, null);
        Assertions.assertEquals(reviewsList.size(), 3);
        List<Reviews> reviewsList1 = reviewsStorage.getReviewsFilm(null, 1L);
        Assertions.assertEquals(1, reviewsList1.size());
        List<Reviews> reviewsList2 = reviewsStorage.getReviewsFilm(1L, null);
        Assertions.assertEquals(2, reviewsList2.size());
    }


    @Test
    public void addLike() {
        Reviews reviews = new Reviews("bbuij", true, user1.getId(), film.getId());
        reviewsStorage.create(reviews);
        reviewsStorage.addLike(1L, 1L);
        reviewsStorage.addLike(1L, 2L);
        reviewsStorage.addLike(1L, 3L);
        Reviews reviews1 = reviewsStorage.get(1L);
        Assertions.assertEquals(reviews1.getUseful(), 3);
    }


    @Test
    public void addDislike() {
        Reviews reviews = new Reviews("bbuij", true, user1.getId(), film.getId());
        reviewsStorage.create(reviews);
        reviewsStorage.addDislike(1L, 1L);
        reviewsStorage.addDislike(1L, 2L);
        reviewsStorage.addDislike(1L, 3L);
        Reviews reviews1 = reviewsStorage.get(1L);
        Assertions.assertEquals(reviews1.getUseful(), -3);
    }


    @Test
    public void removeLike() {
        Reviews reviews = new Reviews("bbuij", true, user1.getId(), film.getId());
        reviewsStorage.create(reviews);
        reviewsStorage.addLike(1L, 1L);
        reviewsStorage.addLike(1L, 2L);
        reviewsStorage.addLike(1L, 3L);
        Reviews reviews1 = reviewsStorage.get(1L);
        Assertions.assertEquals(reviews1.getUseful(), 3);
        reviewsStorage.removeLike(1L, 1L);
        Reviews reviews2 = reviewsStorage.get(1L);
        Assertions.assertEquals(reviews2.getUseful(), 2);
    }


    @Test
    public void removeDislike() {
        Reviews reviews = new Reviews("bbuij", true, user1.getId(), film.getId());
        reviewsStorage.create(reviews);
        reviewsStorage.addDislike(1L, 1L);
        reviewsStorage.addDislike(1L, 2L);
        reviewsStorage.addDislike(1L, 3L);
        Reviews reviews1 = reviewsStorage.get(1L);
        Assertions.assertEquals(reviews1.getUseful(), -3);
        reviewsStorage.removeDislike(1L, 1L);
        Reviews reviews2 = reviewsStorage.get(1L);
        Assertions.assertEquals(reviews2.getUseful(), -2);
    }


}
