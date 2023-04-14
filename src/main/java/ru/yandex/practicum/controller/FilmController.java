package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FilmController {
    FilmService filmService;

    @PostMapping("/films") // POST /films
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("/films") // PUT /films
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/films") // GET /films
    public List<Film> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/films/{id}") // GET /films/{id}
    public Film findFilmById(@PathVariable("id") Long id) {
        return filmService.findFilmById(id);
    }

    // PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") @Positive Long filmId,
                        @PathVariable("userId") @Positive Long userId) {
        return filmService.addLike(filmId, userId);
    }

    // DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike(
            @PathVariable("id") @Positive Long filmId,
            @PathVariable("userId") Long userId) {
        return filmService.removeLike(filmId, userId);
    }

    // GET /films/popular?count={count}
    @GetMapping("/films/popular")
    public List<Film> findPopularFilms(
            @RequestParam(defaultValue = "10", required = false) @PositiveOrZero Integer count) {
        return filmService.findPopularFilms(count);
    }

    @GetMapping("/genres") // GET /genres
    public List<Genre> findAllGenres() {
        return filmService.findAllGenres();
    }

    @GetMapping("/genres/{id}") // GET /genres/{id}
    public Genre findGenreById(@PathVariable("id") @Positive Long id) {
        return filmService.findGenreById(id);
    }

    @GetMapping("/mpa") // GET /mpa
    public List<Mpa> findAllMpa() {
        return filmService.findAllMpa();
    }

    @GetMapping("/mpa/{id}") // GET /mpa/{id}
    public Mpa findMpaById(@PathVariable("id") @Positive Long id) {
        return filmService.findMpaById(id);
    }
}