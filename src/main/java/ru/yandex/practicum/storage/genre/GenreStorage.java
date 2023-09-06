package ru.yandex.practicum.storage.genre;

import ru.yandex.practicum.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> findAll();

    Genre findGenreById(Long id);

    void checkGenreExistence(Long id);
}
