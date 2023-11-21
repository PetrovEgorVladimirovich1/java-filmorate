package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> getMpa();

    Mpa getByIdMpa(long id);
}
