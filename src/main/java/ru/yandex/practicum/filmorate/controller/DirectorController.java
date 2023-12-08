package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.mapper.DirectorListMapper;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {

    private final DirectorService directorService;
    private final DirectorMapper directorMapper;
    private final DirectorListMapper directorListMapper;


    @PostMapping
    public DirectorDto createDirectorDto(@Valid @RequestBody DirectorDto director) {
        return directorMapper.mapToDirectorDto(directorService.createDirector(
                directorMapper.mapToDirector(director)));
    }

    @PutMapping
    public DirectorDto updateDirectorDto(@Valid @RequestBody DirectorDto director) {
        return directorMapper.mapToDirectorDto(directorService.updateDirector(
                directorMapper.mapToDirector(director)));
    }

    @DeleteMapping("/{id}")
    public void dellDirectorById(@NotNull @PathVariable long id) {
        directorService.dellDirectorById(id);
    }

    @GetMapping
    public List<DirectorDto> getDirectorsDto() {

        return directorListMapper.toDTOList(directorService.getDirectors());
    }

    @GetMapping("/{id}")
    public DirectorDto getDirectorByIdDto(@NotNull @PathVariable long id) {
        return directorMapper.mapToDirectorDto(directorService.getDirectorDyId(id));
    }
}
