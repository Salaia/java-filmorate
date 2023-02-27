package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private Set<Film> films = new HashSet<>();

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            log.debug("Название фильма не может быть пустым!");
            throw new ValidationException("Название фильма не может быть пустым!");
        } else if (film.getDescription().length() > 200) {
            log.debug("Максимальная длина описания — 200 символов. Длина введенного описания: " + film.getDescription().length());
            throw new ValidationException("Максимальная длина описания — 200 символов. Длина введенного описания: " + film.getDescription().length());
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.debug("Дата релиза — не раньше 28 декабря 1895 года. Введена дата: " + film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года. Введена дата: " + film.getReleaseDate());
        } else if (film.getDuration().isZero() || film.getDuration().isNegative()) {
            log.debug("Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        } else {
            films.add(film);
            log.info("Фильм " + film.getName() + " сохранен!");
            return film;
        }
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            log.debug("Название фильма не может быть пустым!");
            throw new ValidationException("Название фильма не может быть пустым!");
        } else if (film.getDescription().length() > 200) {
            log.debug("Максимальная длина описания — 200 символов. Длина введенного описания: " + film.getDescription().length());
            throw new ValidationException("Максимальная длина описания — 200 символов. Длина введенного описания: " + film.getDescription().length());
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1985, Month.DECEMBER, 28))) {
            log.debug("Дата релиза — не раньше 28 декабря 1895 года. " +
                    "Введена дата: " + film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года. " +
                    "Введена дата: " + film.getReleaseDate());
        } else if (film.getDuration().isZero() || film.getDuration().isNegative()) {
            log.debug("Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        } else {
            films.add(film);
            log.info("Фильм " + film.getName() + " сохранен!");
            return film;
        }
    }

    @GetMapping
    public Set<Film> findAll() {
        return films;
    }

}