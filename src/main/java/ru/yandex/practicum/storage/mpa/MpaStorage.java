package ru.yandex.practicum.storage.mpa;

import ru.yandex.practicum.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> findAll();

    Mpa findMpaById(Long id);

    void checkMpaExistence(Long id);
}
