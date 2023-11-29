package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.model.Reviews;

import java.util.List;

public interface ReviewsStorage {
    Reviews create (Reviews reviews);
    Reviews update(Reviews reviews);
    Reviews remove( Long id);
    Reviews get(Long id);
    List<Reviews> getReviewsFilm(Long id, Long count);
    Reviews addLike(Long id, Long userId);
    Reviews addDislike(Long id, Long userId);
    Reviews removeLike(Long id, Long userId);
    Reviews removeDislike(Long id, Long userId);
}
