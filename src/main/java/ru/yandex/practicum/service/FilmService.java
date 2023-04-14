package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmService {
    final FilmStorage filmStorage;
    final UserStorage userStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(Long filmId, Long userId) {
        userStorage.checkUserExistence(userId);
        filmStorage.checkFilmExistence(filmId);

        filmStorage.addLike(filmId, userId);
        log.info("Like was added to film " + filmId);
        return filmStorage.findFilmById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        userStorage.checkUserExistence(userId);
        filmStorage.checkFilmExistence(filmId);
        filmStorage.removeLike(filmId, userId);
        log.info("Like was removed from film " + filmId);
        return filmStorage.findFilmById(filmId);
    }

    public List<Film> findPopularFilms(Integer count) {
        List<Film> films = filmStorage.findAllFilms();
        if (films.isEmpty()) {
            String message = "No films in our DataBase yet.";
            log.debug(message);
            throw new NotFoundException(message);
        }
        return films.stream()
                .sorted((film1, film2) -> (film2.getRate() - film1.getRate()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film create(Film film) {
        log.info("Film " + film.getName() + " was successfully saved!");
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        filmStorage.checkFilmExistence(film.getId()); // NotFoundException
        log.info("Film " + film.getName() + " was successfully updated!");
        return filmStorage.update(film);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film findFilmById(Long id) {
        filmStorage.checkFilmExistence(id);
        return filmStorage.findFilmById(id);
    }

    public List<Genre> findAllGenres() {
        return filmStorage.findAllGenres();
    }

    public Genre findGenreById(Long id) {
        filmStorage.checkGenreExistence(id);
        return filmStorage.findGenreById(id);
    }

    public List<Mpa> findAllMpa() {
        return filmStorage.findAllMpa();
    }

    public Mpa findMpaById(Long id) {
        filmStorage.checkMpaExistence(id);
        return filmStorage.findMpaById(id);
    }
}