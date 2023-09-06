package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.service.MpaService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping // GET /mpa
    public List<Mpa> findAll() {
        return mpaService.findAll();
    }

    @GetMapping("/{id}") // GET /mpa/{id}
    public Mpa findMpaById(@PathVariable("id") @Positive Long id) {
        return mpaService.findMpaById(id);
    }

}
