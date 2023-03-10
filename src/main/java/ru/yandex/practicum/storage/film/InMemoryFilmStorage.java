package ru.yandex.practicum.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    final Map<Long, Film> films = new HashMap<>();
    Long lastGeneratedId = 0L;

    @Override
    public Film create(Film film) {
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

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            String message = "There's no such film in our DataBase! Please use Create function!";
            log.debug(message);
            throw new NotFoundException(message);
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
            log.info("Film " + film.getName() + " was successfully updated!");
            return film;
        }
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(Long id) {
        if (!films.containsKey(id)) {
            String message = "There's no such film in our DataBase! Please use Create function!";
            log.debug(message);
            throw new NotFoundException(message);
        }
        return films.get(id);
    }

    private Long generateId() {
        return ++lastGeneratedId;
    }
}
