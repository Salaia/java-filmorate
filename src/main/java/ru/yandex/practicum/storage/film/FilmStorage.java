package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> findAllFilms();

    Film findFilmById(Long id);

    Film addLike(Long filmId, Long userId);

    Film removeLike(Long filmId, Long userId);

    void checkFilmExistence(Long id);

    List<Genre> findAllGenres();

    Genre findGenreById(Long id);

    void checkGenreExistence(Long id);

    List<Mpa> findAllMpa();

    Mpa findMpaById(Long id);

    void checkMpaExistence(Long id);

}
