package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private Long lastGeneratedId = 0L;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.debug("Такой пользователь уже существует.");
            throw new ValidationException("Такой пользователь уже существует.");
        } else if (!user.getEmail().contains("@") || user.getEmail().isBlank() || user.getEmail() == null) {
            log.debug("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin() == null || user.getLogin().contains(" ")) {
            log.debug("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            user.setId(generateId());
            users.put(user.getId(), user);
            log.info("Сохранен пользователь под login: " + user.getLogin());
            return user;
        } else {
            user.setId(generateId());
            users.put(user.getId(), user);
            log.info("Сохранен пользователь под login: " + user.getLogin());
            return user;
        }
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.debug("Такого пользователя нет в базе.");
            throw new ValidationException("Такого пользователя нет в базе.");
        } else if (!user.getEmail().contains("@") || user.getEmail().isBlank() || user.getEmail() == null) {
            log.debug("Электронная почта не может быть пустой и должна содержать символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isEmpty() || user.getLogin() == null || user.getLogin().contains(" ")) {
            log.debug("Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            users.replace(user.getId(), user);
            log.info("Сохранен пользователь под login: " + user.getLogin());
            return user;
        } else {
            users.replace(user.getId(), user);
            log.info("Сохранен пользователь под login: " + user.getLogin());
            return user;
        }
    }

    @GetMapping
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    private Long generateId() {
        return ++lastGeneratedId;
    }
}
