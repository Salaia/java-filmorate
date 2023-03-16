/*
package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@WebMvcTest(controllers = FilmController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
     static FilmController controller;
    @Autowired
     static FilmService service;
    @Autowired static FilmStorage filmStorage;
    @Autowired static UserStorage userStorage;

    @BeforeEach
    void init() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        service = new FilmService(filmStorage, userStorage);
        controller = new FilmController(service);
    }

    @Test
    void filmCreateSuccess() {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        assertFalse(controller.findAll().contains(filmOriginal));
        controller.create(filmOriginal);
        assertTrue(controller.findAll().contains(filmOriginal));
    }

    @Test
    void descriptionTooLongFailCreate() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .content("{\"name\":\"Harry Potter and the Philosopher's Stone\"," +
                                "\"description\":\"The Boy Who Lived Harry Potter and the Philosopher's Stone \" +\n" +
                                "                \"Harry Potter and the Philosopher's Stone Harry Potter and the Philosopher's Stone \" +\n" +
                                "                \"Harry Potter and the Philosopher's Stone Harry Potter and the Philosopher's Stone \" +\n" +
                                "                \"Harry Potter and the Philosopher's Stone\"," +
                                "\"releaseDate\": \"2001-11-04\"," +
                                "\"duration\": \"152\"}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


        */
/*Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived Harry Potter and the Philosopher's Stone " +
                "Harry Potter and the Philosopher's Stone Harry Potter and the Philosopher's Stone " +
                "Harry Potter and the Philosopher's Stone Harry Potter and the Philosopher's Stone " +
                "Harry Potter and the Philosopher's Stone");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Film description max length is 200 symbols. Current input is: " + filmOriginal.getDescription().length() + " symbols.");
            hasException = true;
        }
        assertTrue(hasException);*//*

    }

    @Test
    void blankNameFailCreate() {
        Film filmOriginal = new Film();
        filmOriginal.setName("   ");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Film name may not be empty!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wrongDateFailCreate() {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(1001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Release date may not be before 1895, December, 28th. Current input is: "
                    + filmOriginal.getReleaseDate());
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wrongDurationFailCreate() {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(-152);
        // System.out.println(filmOriginal.toString());
        boolean hasException = false;
        try {
            controller.create(filmOriginal);
            System.out.println(filmOriginal);
        } catch (Throwable e) {
            assertEquals(e.getMessage(), "Film duration has to be positive.");
            hasException = true;
        }
        assertTrue(hasException);
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
    void descriptionTooLongFailUpdate() {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
            filmOriginal.setDescription("The Boy Who Lived Harry Potter and the Philosopher's Stone " +
                    "Harry Potter and the Philosopher's Stone Harry Potter and the Philosopher's Stone " +
                    "Harry Potter and the Philosopher's Stone Harry Potter and the Philosopher's Stone " +
                    "Harry Potter and the Philosopher's Stone");
            controller.update(filmOriginal);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Film description max length is 200 symbols. Current input is: "
                    + filmOriginal.getDescription().length() + " symbols.");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void blankNameFailUpdate() {
        Film filmOriginal = new Film();
        filmOriginal.setName("Potter");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
            filmOriginal.setName("   ");
            controller.update(filmOriginal);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Film name may not be empty!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wrongDateFailUpdate() {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(1001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
            controller.update(filmOriginal);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Release date may not be before 1895, December, 28th. Current input is: "
                    + filmOriginal.getReleaseDate());
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wrongDurationFailUpdate() {
        Film filmOriginal = new Film();
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
            filmOriginal.setDuration(-152);
            controller.update(filmOriginal);
        } catch (ValidationException e) {
            System.out.println(controller.findAll());
            assertEquals(e.getMessage(), "Film duration has to be positive.");
            hasException = true;
        }
        assertTrue(hasException);
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
}*/
