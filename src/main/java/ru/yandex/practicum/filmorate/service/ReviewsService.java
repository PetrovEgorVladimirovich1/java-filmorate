package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Reviews;
import ru.yandex.practicum.filmorate.storage.dal.ReviewsStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewsService {

    private final ReviewsStorage reviewsStorage;

    @Autowired
    public ReviewsService(ReviewsStorage reviewsStorage) {
        this.reviewsStorage = reviewsStorage;
    }

    public Reviews create(Reviews reviews) {
        if (reviews.getUserId() <= 0 || reviews.getFilmId() <= 0) {
            throw new IncorrectParamException("id меньше нуля");
        }
        return reviewsStorage.create(reviews);
    }

    public Reviews update(Reviews reviews) {
        if (get(reviews.getReviewId()) == null) {
            throw new ValidationException("Not found key: " + reviews.getReviewId());
        }
        return reviewsStorage.update(reviews);
    }


    public Reviews remove(Long id) {
        return reviewsStorage.remove(id);
    }

    public Reviews get(Long id) {
        return reviewsStorage.get(id);
    }

    public List<Reviews> getReviewsFilm(Long id, Long count) {
        return reviewsStorage.getReviewsFilm(id, count)
                .stream()
                .sorted((o1, o2) -> o2.getUseful() - o1.getUseful())
                .collect(Collectors.toList());

    }

    public Reviews addLike(Long id, Long userId) {
        return reviewsStorage.addLike(id, userId);
    }

    public Reviews addDislike(Long id, Long userId) {
        return reviewsStorage.addDislike(id, userId);
    }

    public Reviews removeLike(Long id, Long userId) {
        return reviewsStorage.removeLike(id, userId);
    }

    public Reviews removeDislike(Long id, Long userId) {
        return reviewsStorage.removeDislike(id, userId);
    }
}
