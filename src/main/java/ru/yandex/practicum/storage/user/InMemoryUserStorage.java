package ru.yandex.practicum.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;

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
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.replace(user.getId(), user);
        return user;
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
