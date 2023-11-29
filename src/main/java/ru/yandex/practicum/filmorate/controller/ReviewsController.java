package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Reviews;
import ru.yandex.practicum.filmorate.service.ReviewsService;

import java.util.List;

@Slf4j
@Component
@RestController
@RequestMapping(value = "/reviews")
public class ReviewsController {
    private final ReviewsService reviewsService;

    @Autowired
    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @PostMapping
    public Reviews create(@RequestBody Reviews reviews) {
        log.info("Пришел POST запрос /reviews с телом: {}", reviews);
        Reviews response = reviewsService.create(reviews);
        log.info("Отправлен ответ для POST запроса /reviews с телом: {}", response);
        return response;
    }

    @PutMapping
    public Reviews update(@RequestBody Reviews reviews) {
        log.info("Пришел PUT запрос /reviews с телом: {}", reviews);
        Reviews response = reviewsService.update(reviews);
        log.info("Отправлен ответ для PUT запроса /reviews с телом: {}", response);
        return response;
    }


    @DeleteMapping(value = "/{id}")
    public Reviews remove(@PathVariable Long id) {
        log.info("Пришел DELETE запрос /reviews с телом: {}", id);
        Reviews response = reviewsService.remove(id);
        log.info("Отправлен ответ для DELETE запроса /reviews с телом: {}", response);
        return response;
    }


    @GetMapping(value = "/{id}")
    public Reviews get(@PathVariable Long id) {
        log.info("Пришел GET запрос /reviews с телом: {}", id);
        Reviews response = reviewsService.get(id);
        log.info("Отправлен ответ для GET запроса /reviews с телом: {}", response);
        return response;
    }


    @GetMapping(value = "?filmId={id}&count={count}")
    public List<Reviews> getReviewsFilm(@PathVariable Long id, @PathVariable Long count) {
        log.info("Пришел GET запрос ?filmId={}&count={}", id, count);
        List<Reviews> response = reviewsService.getReviewsFilm(id, count);
        log.info("Отправлен ответ для GET запроса ?filmId={}&count={}", id, count);
        return response;
    }


    @PutMapping(value = "/{id}/like/{userId}")
    public Reviews addLike(@PathVariable Long id,@PathVariable Long userId) {
        log.info("Пришел PUT запрос /{}/like/{}", id, userId);
        Reviews response = reviewsService.addLike(id, userId);
        log.info("Отправлен ответ для PUT запроса /{}/like/{}", id, userId);
        return response;
    }


    @PutMapping(value = "/{id}/dislike/{userId}")
    public Reviews addDislike(@PathVariable Long id,@PathVariable Long userId) {
        log.info("Пришел PUT запрос /{}/dislike/{}", id, userId);
        Reviews response = reviewsService.addDislike(id, userId);
        log.info("Отправлен ответ для PUT запроса /{}/dislike/{}", id, userId);
        return response;
    }


    @DeleteMapping(value = "/{id}/like/{userId}")
    public Reviews removeLike(@PathVariable Long id,@PathVariable Long userId) {
        log.info("Пришел DELETE запрос /{}/like/{}", id, userId);
        Reviews response = reviewsService.removeLike(id, userId);
        log.info("Отправлен ответ для DELETE запроса /{}/like/{}", id, userId);
        return response;
    }


    @DeleteMapping("/{id}/dislike/{userId}")
    public Reviews removeDislike(@PathVariable Long id,@PathVariable Long userId) {
        log.info("Пришел DELETE запрос /{}/dislike/{}", id, userId);
        Reviews response = reviewsService.removeDislike(id, userId);
        log.info("Отправлен ответ для DELETE запроса /{}/dislike/{}", id, userId);
        return response;
    }
}
