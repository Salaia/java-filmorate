package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class FilmControllerTest {
    static FilmController controller;
    static FilmService service;

    @BeforeEach
    void init() {
        service = new FilmService();
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
    void descriptionTooLongFailCreate() {
        Film filmOriginal = new Film();
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
        assertTrue(hasException);
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

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
        } catch (ValidationException e) {
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
}