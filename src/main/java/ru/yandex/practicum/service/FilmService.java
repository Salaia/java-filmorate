package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    public Film addLike(Long filmId, Long userId) {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);
        film.getLikes().add(userId);
        log.info("Like was added to film " + filmStorage.findFilmById(filmId).getName());
        return filmStorage.findFilmById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);
        film.getLikes().remove(userId);
        log.info("Like was removed from film " + filmStorage.findFilmById(filmId).getName());
        return filmStorage.findFilmById(filmId);
    }

    public List<Film> findPopularFilms(Integer count) {
        List<Film> films = filmStorage.findAll();
        if (films.isEmpty()) {
            String message = "No films in our DataBase yet.";
            log.debug(message);
            throw new NotFoundException(message);
        }
        return films.stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film create(Film film) {
        log.info("Film " + film.getName() + " was successfully saved!");
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        filmStorage.findFilmById(film.getId()); // NotFoundException
        log.info("Film " + film.getName() + " was successfully updated!");
        return filmStorage.update(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }
}