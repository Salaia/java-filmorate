package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Mpa;

import java.util.List;

public interface MpaDao {

    List<Mpa> findAll();

    Mpa findMpaById(Long id);
}
