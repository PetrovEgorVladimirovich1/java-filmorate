package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.mapper.GenreListMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;
    private final GenreMapper genreMapper;
    private final GenreListMapper genreListMapper;

    @GetMapping
    public List<GenreDto> getGenresDto() {
        return genreListMapper.toDTOList(genreService.getGenres());
    }

    @GetMapping("/{id}")
    public GenreDto getByIdGenreDto(@PathVariable long id) {
        return genreMapper.mapToDto(genreService.getByIdGenre(id));
    }
}
