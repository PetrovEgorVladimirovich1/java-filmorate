package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 0;

    private long addId() {
        return ++id;
    }

    @Override
    public void create(Film film) {
        film.setId(addId());
        films.put(film.getId(), film);
    }

    @Override
    public void update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new IncorrectParamException("Неверный id!");
        }
        films.put(film.getId(), film);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getByIdFilm(long id) {
        return films.getOrDefault(id, null);
    }

    @Override
    public void addLike(long idFilm, long idUser) {
    }

    @Override
    public void deleteLike(long idFilm, long idUser) {
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return null;
    }
}
