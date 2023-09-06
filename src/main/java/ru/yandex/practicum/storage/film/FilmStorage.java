package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> findAllFilms();

    Film findFilmById(Long id);

    Film addLike(Long filmId, Long userId);

    Film removeLike(Long filmId, Long userId);

    void checkFilmExistence(Long id);

}
