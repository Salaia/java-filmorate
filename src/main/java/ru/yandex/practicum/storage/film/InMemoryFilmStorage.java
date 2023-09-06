package ru.yandex.practicum.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    final Map<Long, Film> films = new HashMap<>();
    Long lastGeneratedId = 0L;

    @Override
    public Film create(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.replace(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(Long id) {
        checkFilmExistence(id);
        return films.get(id);
    }

    public Film addLike(Long filmId, Long userId) {
        return null;
    }

    public Film removeLike(Long filmId, Long userId) {
        return null;
    }

    public void checkFilmExistence(Long id) {
        if (!films.containsKey(id)) {
            String message = "There's no such film in our DataBase! Please use Create function!";
            log.debug(message);
            throw new NotFoundException(message);
        }
    }

    public List<Genre> findAllGenres() {
        return null;
    }

    public Genre findGenreById(Long id) {
        return null;
    }

    public void checkGenreExistence(Long id) {
    }

    public List<Mpa> findAllMpa() {
        return null;
    }

    public Mpa findMpaById(Long id) {
        return null;
    }

    public void checkMpaExistence(Long id) {
    }

    private Long generateId() {
        return ++lastGeneratedId;
    }
}
