package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.dal.DirectorStorage;

import java.util.List;

@Slf4j
@Service
public class DirectorService {
    private final DirectorStorage directorStorage;

    @Autowired
    public DirectorService(DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public Director createDirector(Director director) {
        if (director.getName() == null || director.getName().isBlank()) {
            throw new ValidationException("Отсутствует имя режиссёра");
        }
        Director director1 = directorStorage.createDirector(director);
        log.info("Режиссёр успешно создан. {}", director1);
        return director1;
    }

    public Director updateDirector(Director director) {
        Director director1 = directorStorage.updateDirector(director);
        log.info("Режиссёр успешно обновлён. {}", director1);
        return director1;
    }

    public void dellDirectorById(long id) {
        directorStorage.dellDirectorById(id);
        log.info("Режиссёр удалён.");
    }

    public List<Director> getDirectors() {
        return directorStorage.getDirectors();
    }

    public Director getDirectorDyId(long id) {
        return directorStorage.getDirectorById(id);
    }
}
