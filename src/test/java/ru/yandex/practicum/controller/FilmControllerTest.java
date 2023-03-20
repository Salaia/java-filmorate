package ru.yandex.practicum.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;
    static FilmController controller;
    static FilmService service;
    static FilmStorage filmStorage;
    static UserStorage userStorage;
    static final ObjectMapper objectMapper =
            new ObjectMapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    @BeforeEach
    void init() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        service = new FilmService(filmStorage, userStorage);
        controller = new FilmController(service);
    }

    public static String asJsonString(final Object obj) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void filmCreateSuccess() throws Exception {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filmOriginal)))
                .andExpect(status()
                        .isOk());
    }

    @Test
    void descriptionTooLongFailCreate() throws Exception {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived The Boy Who Lived The Boy Who Lived" +
                "The Boy Who Lived The Boy Who Lived The Boy Who Lived" +
                "The Boy Who Lived The Boy Who Lived The Boy Who Lived" +
                "The Boy Who Lived The Boy Who Lived The Boy Who Lived" +
                "The Boy Who Lived The Boy Who Lived The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filmOriginal)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void blankNameFailCreate() throws Exception {
        Film filmOriginal = new Film();
        filmOriginal.setName("   ");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filmOriginal)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void wrongDateFailCreate() throws Exception {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(1001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filmOriginal)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void wrongDurationFailCreate() throws Exception {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(-152);
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filmOriginal)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void filmUpdateSuccess() {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        assertFalse(controller.findAll().contains(filmOriginal));
        controller.create(filmOriginal);
        assertTrue(controller.findAll().contains(filmOriginal));
        filmOriginal.setDescription("Update");
        assertEquals(filmOriginal.getDescription(), "Update");
    }

    @Test
    void descriptionTooLongFailUpdate() throws Exception {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);
        controller.create(filmOriginal);

        filmOriginal.setDescription("The Boy Who Lived Harry Potter and the Philosopher's Stone " +
                "Harry Potter and the Philosopher's Stone Harry Potter and the Philosopher's Stone " +
                "Harry Potter and the Philosopher's Stone Harry Potter and the Philosopher's Stone " +
                "Harry Potter and the Philosopher's Stone");
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filmOriginal)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void blankNameFailUpdate() throws Exception {
        Film filmOriginal = new Film();
        filmOriginal.setName("Potter");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);
        controller.create(filmOriginal);

        filmOriginal.setName("   ");
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filmOriginal)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void wrongDateFailUpdate() throws Exception {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);
        controller.create(filmOriginal);

        filmOriginal.setReleaseDate(LocalDate.of(888, Month.JANUARY, 12));
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filmOriginal)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void wrongDurationFailUpdate() throws Exception {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);
        controller.create(filmOriginal);

        filmOriginal.setDuration(-152);
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(filmOriginal)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void getFilmByIdSuccess() {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);
        controller.create(filmOriginal);

        Long id = filmOriginal.getId();
        Film filmRestored = controller.findFilmById(id);
        assertEquals(filmOriginal, filmRestored);
    }
}
