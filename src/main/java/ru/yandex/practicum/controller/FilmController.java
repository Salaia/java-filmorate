package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.storage.film.FilmStorage;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class FilmController {
    final FilmService filmService;
    final FilmStorage filmStorage;

    @PostMapping // POST /films
    public Film create(@RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping // PUT /films
    public Film update(@RequestBody Film film) {
        return filmStorage.update(film);
    }

    @GetMapping // GET /films
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/{id}") // GET /films/{id}
    public Film findFilmById(@PathVariable("id") Long id) {
        return filmStorage.findFilmById(id);
    }

    // PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    @PutMapping("{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        return filmService.addLike(filmId, userId);
    }

    // DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        return filmService.removeLike(filmId, userId);
    }

    // GET /films/popular?count={count}
    @GetMapping("/popular")
    public List<Film> findPopularFilms(
            @RequestParam(defaultValue = "10", required = false) Integer count) {
        // 10 здесь не считается magic number? или лучше заменить на константу?
        return filmService.findPopularFilms(count);
    }
}