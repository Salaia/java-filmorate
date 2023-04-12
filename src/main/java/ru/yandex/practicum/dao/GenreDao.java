package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> findAll();

    Genre findGenreById(Long id);

}
