package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Primary
@Validated
@RequestMapping("/films")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FilmController {
    FilmService filmService;

    @PostMapping // POST /films
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping // PUT /films
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping // GET /films
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}") // GET /films/{id}
    public Film findFilmById(@PathVariable("id") Long id) {
        return filmService.findFilmById(id);
    }

    // PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    @PutMapping("{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Long filmId,
                        @PathVariable("userId") Long userId) {
        return filmService.addLike(filmId, userId);
    }

    // DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(
            @PathVariable("id") Long filmId,
            @PathVariable("userId") Long userId) {
        return filmService.removeLike(filmId, userId);
    }

    // GET /films/popular?count={count}
    @GetMapping("/popular")
    public List<Film> findPopularFilms(
            @RequestParam(defaultValue = "10", required = false) @PositiveOrZero Integer count) {
        return filmService.findPopularFilms(count);
    }
}