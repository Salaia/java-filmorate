package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmService {
    final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLike(Long filmId, Long userId) {
        if (userId <= 0) {
            String message = "Incorrect user id.";
            log.debug(message);
            throw new NotFoundException(message);
        }
        if (filmId <= 0) {
            String message = "Incorrect film id.";
            log.debug(message);
            throw new NotFoundException(message);
        }
        filmStorage.findFilmById(filmId).getLikes().add(userId);
        log.info("Like was added to film " + filmStorage.findFilmById(filmId).getName());
        return filmStorage.findFilmById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        if (userId <= 0) {
            String message = "Incorrect user id.";
            log.debug(message);
            throw new NotFoundException(message);
        }
        if (filmId <= 0) {
            String message = "Incorrect film id.";
            log.debug(message);
            throw new NotFoundException(message);
        }
        filmStorage.findFilmById(filmId).getLikes().remove(userId);
        log.info("Like was removed from film " + filmStorage.findFilmById(filmId).getName());
        return filmStorage.findFilmById(filmId);
    }

    public List<Film> findPopularFilms(Integer count) {
        if (filmStorage.findAll().isEmpty()) {
            String message = "No films in our DataBase yet.";
            log.debug(message);
            throw new NotFoundException(message);
        }
        return filmStorage.findAll().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}