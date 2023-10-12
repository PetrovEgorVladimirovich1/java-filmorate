package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FilmControllerTest {

    private static final String PATH = "/films";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void create() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContent("controller/request/film/film.json")))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createFilmFailName() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContent("controller/request/film/filmFailName.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createFilmFailDescription() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContent("controller/request/film/filmFailDescription.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createFilmFailDuration() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContent("controller/request/film/filmFailDuration.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createFilmFailReleaseDate() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContent("controller/request/film/filmFailReleaseDate.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    private String getContent(String file) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + file).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}