package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Film;

import java.util.List;

public interface FilmDao {
    Film create(Film film);

    Film update(Film film);

    List<Film> findAll();

    Film findFilmById(Long id);

    Film addLike(Long filmId, Long userId);

    Film removeLike(Long filmId, Long userId);

    void checkFilmExistence(Long id);
}
