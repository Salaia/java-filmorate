package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> findAll();

    Film findFilmById(Long id);
}
