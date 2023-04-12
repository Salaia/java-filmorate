package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.service.GenreService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping // GET /genres
    public List<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}") // GET /genres/{id}
    public Genre findGenreById(@PathVariable("id") @Positive Long id) {
        return genreService.findGenreById(id);
    }

}
