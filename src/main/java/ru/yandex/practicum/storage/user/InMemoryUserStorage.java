package ru.yandex.practicum.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    final Map<Long, User> users = new HashMap<>();
    Long lastGeneratedId = 0L;

    @Override
    public User create(User user) {
        if (users.containsKey(user.getId())) {
            String message = "User already exists in our DataBase. Please use Update function.";
            log.debug(message);
            throw new ValidationException(message);
        } else if (!user.getEmail().contains("@") || user.getEmail().isBlank() || user.getEmail() == null) {
            String message = "Email may not be empty and has to contain '@' symbol!";
            log.debug(message);
            throw new ValidationException(message);
        } else if (user.getLogin().isEmpty() || user.getLogin() == null || user.getLogin().contains(" ")) {
            String message = "Login may not be empty or contain any space-symbols!";
            log.debug(message);
            throw new ValidationException(message);
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            String message = "Date of birth may not be in future!";
            log.debug(message);
            throw new ValidationException(message);
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            user.setId(generateId());
            users.put(user.getId(), user);
            log.info("User was successfully saved under login: " + user.getLogin());
            return user;
        } else {
            user.setId(generateId());
            users.put(user.getId(), user);
            log.info("User was successfully saved under login: " + user.getLogin());
            return user;
        }
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            String message = "There's no such user in our DataBase!";
            log.debug(message);
            throw new NotFoundException(message);
        } else if (!user.getEmail().contains("@") || user.getEmail().isBlank() || user.getEmail() == null) {
            String message = "Email may not be empty and has to contain '@' symbol!";
            log.debug(message);
            throw new ValidationException(message);
        } else if (user.getLogin().isEmpty() || user.getLogin() == null || user.getLogin().contains(" ")) {
            String message = "Login may not be empty or contain any space-symbols!";
            log.debug(message);
            throw new ValidationException(message);
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            String message = "Date of birth may not be in future!";
            log.debug(message);
            throw new ValidationException(message);
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            users.replace(user.getId(), user);
            log.info("User was successfully updated under login: " + user.getLogin());
            return user;
        } else {
            users.replace(user.getId(), user);
            log.info("User was successfully updated under login: " + user.getLogin());
            return user;
        }
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(Long id) {
        if (!users.containsKey(id)) {
            String message = "There's no such user in our DataBase!";
            log.debug(message);
            throw new NotFoundException(message);
        } else return users.get(id);
    }

    private Long generateId() {
        return ++lastGeneratedId;
    }
}
