package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.mapper.MpaListMapper;
import ru.yandex.practicum.filmorate.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;
    private final MpaMapper mpaMapper;
    private final MpaListMapper mpaListMapper;

    @GetMapping
    public List<MpaDto> getMpaDto() {
        return mpaListMapper.toDTOList(mpaService.getMpa());
    }

    @GetMapping("/{id}")
    public MpaDto getByIdMpaDto(@PathVariable long id) {
        return mpaMapper.mapToDto(mpaService.getByIdMpa(id));
    }
}
