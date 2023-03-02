package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmService {

    final Map<Long, Film> films = new HashMap<>();
    Long lastGeneratedId = 0L;

    public Film create(@RequestBody @Valid Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            String message = "Film name may not be empty!";
            log.debug(message);
            throw new ValidationException(message);
        } else if (film.getDescription().length() > 200) {
            String message = "Film description max length is 200 symbols. Current input is: " + film.getDescription().length() + " symbols.";
            log.debug(message);
            throw new ValidationException(message);
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            String message = "Release date may not be before 1895, December, 28th. Current input is: " + film.getReleaseDate();
            log.debug(message);
            throw new ValidationException(message);
        } else if (film.getDuration() <= 0) {
            String message = "Film duration has to be positive.";
            log.debug(message);
            throw new ValidationException(message);
        } else {
            film.setId(generateId());
            films.put(film.getId(), film);
            log.info("Film " + film.getName() + " was successfully saved!");
            return film;
        }
    }

    public Film update(@RequestBody @Valid Film film) {
        if (!films.containsKey(film.getId())) {
            String message = "There's no such film in our DataBase! Please use Create function!";
            log.debug(message);
            throw new ValidationException(message);
        }
        if (film.getName().isBlank() || film.getName() == null) {
            String message = "Film name may not be empty!";
            log.debug(message);
            throw new ValidationException(message);
        } else if (film.getDescription().length() > 200) {
            String message = "Film description max length is 200 symbols. Current input is: " + film.getDescription().length() + " symbols.";
            log.debug(message);
            throw new ValidationException(message);
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1985, Month.DECEMBER, 28))) {
            String message = "Release date may not be before 1895, December, 28th. Current input is: " + film.getReleaseDate();
            log.debug(message);
            throw new ValidationException(message);
        } else if (film.getDuration() <= 0) {
            String message = "Film duration has to be positive.";
            log.debug(message);
            throw new ValidationException(message);
        } else {
            films.replace(film.getId(), film);
            log.info("Film " + film.getName() + " was successfully saved!");
            return film;
        }
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    private Long generateId() {
        return ++lastGeneratedId;
    }

}