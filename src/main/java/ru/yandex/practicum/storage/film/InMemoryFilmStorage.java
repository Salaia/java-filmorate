package ru.yandex.practicum.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Primary
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    final Map<Long, Film> films = new HashMap<>();
    Long lastGeneratedId = 0L;

    @Override
    public Film create(@Valid Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Film " + film.getName() + " was successfully saved!");
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            String message = "There's no such film in our DataBase! Please use Create function!";
            log.debug(message);
            throw new NotFoundException(message);
        }
        films.replace(film.getId(), film);
        log.info("Film " + film.getName() + " was successfully updated!");
        return film;
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
