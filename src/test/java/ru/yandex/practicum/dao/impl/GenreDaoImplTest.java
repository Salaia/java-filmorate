package ru.yandex.practicum.dao.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDaoImplTest {

    private final GenreDaoImpl genreDao;

    private final MockMvc mockMvc;
    static List<Genre> genreList = new ArrayList<>();
    static final ObjectMapper objectMapper =
            new ObjectMapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    public static String asJsonString(final Object obj) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void fillInGenreList() {
        genreList.add(new Genre(1L, "Комедия"));
        genreList.add(new Genre(2L, "Драма"));
        genreList.add(new Genre(3L, "Мультфильм"));
        genreList.add(new Genre(4L, "Триллер"));
        genreList.add(new Genre(5L, "Документальный"));
        genreList.add(new Genre(6L, "Боевик"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findGenreByIdSuccess() throws Exception {
        Genre comedy = Genre.builder()
                .id(1L)
                .name("Комедия")
                .build();

        Genre testGenre = genreDao.findGenreById(1L);
        assertEquals(comedy, testGenre);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void failFindGenreByWrongId() throws Exception {
        mockMvc
                .perform(get("/genres/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findAllGenresSuccess() {
        fillInGenreList();
        assertEquals(genreList, genreDao.findAll());
    }
}