package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    static FilmController controller;

    @BeforeEach
    void init() {
        controller = new FilmController();
    }

    @Test
    void filmCreateSuccess() {
        Film filmOriginal = new Film();
        filmOriginal.setId(1L);
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
        filmOriginal.setId(1L);
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
            assertEquals(e.getMessage(), "Максимальная длина описания — 200 символов. " +
                    "Длина введенного описания: " + filmOriginal.getDescription().length());
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void blankNameFailCreate() {
        Film filmOriginal = new Film();
        filmOriginal.setId(1L);
        filmOriginal.setName("   ");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Название фильма не может быть пустым!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wrongDateFailCreate() {
        Film filmOriginal = new Film();
        filmOriginal.setId(1L);
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(1001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза — не раньше 28 декабря 1895 года. " +
                    "Введена дата: " + filmOriginal.getReleaseDate());
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wrongDurationFailCreate() {
        Film filmOriginal = new Film();
        filmOriginal.setId(1L);
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(2001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(-152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Продолжительность фильма должна быть положительной.");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void filmUpdateSuccess() {
        Film filmOriginal = new Film();
        filmOriginal.setId(1L);
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
        filmOriginal.setId(1L);
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
            assertEquals(e.getMessage(), "Максимальная длина описания — 200 символов. " +
                    "Длина введенного описания: " + filmOriginal.getDescription().length());
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void blankNameFailUpdate() {
        Film filmOriginal = new Film();
        filmOriginal.setId(1L);
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
            assertEquals(e.getMessage(), "Название фильма не может быть пустым!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wrongDateFailUpdate() {
        Film filmOriginal = new Film();
        filmOriginal.setId(1L);
        filmOriginal.setName("Harry Potter and the Philosopher's Stone");
        filmOriginal.setDescription("The Boy Who Lived");
        filmOriginal.setReleaseDate(LocalDate.of(1001, Month.NOVEMBER, 4));
        filmOriginal.setDuration(152);

        boolean hasException = false;
        try {
            controller.create(filmOriginal);
            controller.update(filmOriginal);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата релиза — не раньше 28 декабря 1895 года. " +
                    "Введена дата: " + filmOriginal.getReleaseDate());
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wrongDurationFailUpdate() {
        Film filmOriginal = new Film();
        filmOriginal.setId(1L);
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
            assertEquals(e.getMessage(), "Продолжительность фильма должна быть положительной.");
            hasException = true;
        }
        assertTrue(hasException);
    }
}