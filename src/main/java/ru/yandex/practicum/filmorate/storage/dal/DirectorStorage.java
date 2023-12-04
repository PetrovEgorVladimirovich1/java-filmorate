package ru.yandex.practicum.filmorate.storage.dal;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {

    void createDirector(Director director);

    void updateDirector(Director director);

    void dellDirectorById(long id);

    Director getDirectorById(long id);

    List<Director> getDirectors();
}
