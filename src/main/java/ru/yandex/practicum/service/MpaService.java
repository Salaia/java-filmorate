package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.MpaDao;
import ru.yandex.practicum.model.Mpa;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaDao mpaDao;

    public List<Mpa> findAll() {
        return mpaDao.findAll();
    }

    public Mpa findMpaById(Long id) {
        return mpaDao.findMpaById(id);
    }

}
