package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.DirectorDto;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.utils.MappingUtils;

import java.util.List;

@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;
    private final MappingUtils mappingUtils;


    @Autowired
    public DirectorController(DirectorService directorService, MappingUtils mappingUtils) {
        this.directorService = directorService;
        this.mappingUtils = mappingUtils;
    }

    @PostMapping
    public DirectorDto createDirector(@RequestBody DirectorDto director) {
        return mappingUtils.mapToDirectorDto(directorService
                .createDirector(mappingUtils.mapToDirector(director)));
    }

    @PutMapping
    public DirectorDto updateDirector(@RequestBody DirectorDto director) {
        return mappingUtils.mapToDirectorDto(directorService
                .updateDirector(mappingUtils.mapToDirector(director)));
    }

    @DeleteMapping("/{id}")
    public void dellDirectorById(@PathVariable long id) {
        directorService.dellDirectorById(id);
    }

    @GetMapping
    public List<DirectorDto> getDirectors() {
        List<Director> directors = directorService.getDirectors();
        return mappingUtils.convertList(directors, mappingUtils::mapToDirectorDto);
    }

    @GetMapping("/{id}")
    public DirectorDto getDirectorById(@PathVariable long id) {
        return mappingUtils.mapToDirectorDto(directorService.getDirectorDyId(id));
    }
}
