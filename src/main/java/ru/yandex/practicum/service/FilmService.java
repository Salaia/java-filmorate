package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    public Film addLike(@Positive Long filmId, @Positive Long userId) {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);
        film.getLikes().add(userId);
        log.info("Like was added to film " + filmStorage.findFilmById(filmId).getName());
        return filmStorage.findFilmById(filmId);
    }

    public Film removeLike(@Positive Long filmId, @Positive Long userId) {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);
        film.getLikes().remove(userId);
        log.info("Like was removed from film " + filmStorage.findFilmById(filmId).getName());
        return filmStorage.findFilmById(filmId);
    }

    public List<Film> findPopularFilms(@PositiveOrZero Integer count) {
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

    public Film create(@Valid Film film) {
        return filmStorage.create(film);
    }

    public Film update(@Valid Film film) {
        return filmStorage.update(film);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilmById(@Positive Long id) {
        return filmStorage.findFilmById(id);
    }

}