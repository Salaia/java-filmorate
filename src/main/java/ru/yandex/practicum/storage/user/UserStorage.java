package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> findAll();

    User findUserById(Long id);
}
