package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(Long userId, Long friendId) {
        userStorage.checkUserExistence(userId);
        userStorage.checkUserExistence(friendId);

        userStorage.addFriend(userId, friendId);
        log.info("Users " + userId +
                " and " + friendId +
                " are friends now!");
        return userStorage.findUserById(friendId);
    }

    public User removeFriend(Long userId, Long friendId) {
        userStorage.checkUserExistence(userId);
        userStorage.checkUserExistence(friendId);
        userStorage.removeFriend(userId, friendId);
        log.info("Users " + userId +
                " and " + friendId +
                " are not friends anymore!");
        return userStorage.findUserById(friendId);
    }

    public List<User> findFriends(Long userId) {
        userStorage.checkUserExistence(userId);
        return userStorage.findFriends(userId);
    }

    public List<User> findCommonFriends(Long userId, Long otherUserId) {
        return userStorage.findCommonFriends(userId, otherUserId);
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("User was successfully saved under login: " + user.getLogin());
        return userStorage.create(user);
    }

    public User update(User user) {
        userStorage.checkUserExistence(user.getId()); // NotFoundException
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("User was successfully updated under login: " + user.getLogin());
        return userStorage.update(user);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findUserById(Long id) {
        userStorage.checkUserExistence(id);
        return userStorage.findUserById(id);
    }
}
