package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getGenres();

    Genre getByIdGenre(long id);
}
