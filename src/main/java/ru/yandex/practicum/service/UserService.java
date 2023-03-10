package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /*
    Пока пользователям не надо одобрять заявки в друзья — добавляем сразу.
    То есть если Лена стала другом Саши, то это значит, что Саша теперь друг Лены.
     */
    public User addFriend(Long userId, Long friendId) {
        if (userId <= 0) {
            String message = "Incorrect user id.";
            log.debug(message);
            throw new NotFoundException(message);
        }
        if (friendId <= 0) {
            String message = "Incorrect friend's id.";
            log.debug(message);
            throw new NotFoundException(message);
        }
        userStorage.findUserById(userId).getFriendsIds().add(friendId);
        userStorage.findUserById(friendId).getFriendsIds().add(userId);
        log.info("Users " + userStorage.findUserById(userId).getName() +
                " and " + userStorage.findUserById(friendId).getName() +
                " are friends now!");
        return userStorage.findUserById(friendId);
    }

    public User removeFriend(Long userId, Long friendId) {
        if (userId <= 0) {
            String message = "Incorrect user id";
            log.debug(message);
            throw new NotFoundException(message);
        }
        if (friendId <= 0) {
            String message = "Incorrect friend's id";
            log.debug(message);
            throw new NotFoundException(message);
        }
        userStorage.findUserById(userId).getFriendsIds().remove(friendId);
        userStorage.findUserById(friendId).getFriendsIds().remove(userId);
        log.info("Users " + userStorage.findUserById(userId).getName() +
                " and " + userStorage.findUserById(friendId).getName() +
                " are not friends anymore!");
        return userStorage.findUserById(friendId);
    }

    public List<User> findFriends(Long userId) {
        if (userId <= 0) {
            String message = "Incorrect user id";
            log.debug(message);
            throw new NotFoundException(message);
        }
        List<User> result = new ArrayList<>();
        for (Long friendId : userStorage.findUserById(userId).getFriendsIds()) {
            result.add(userStorage.findUserById(friendId));
        }
        return result;
    }

    public List<User> findCommonFriends(Long userId, Long otherUserId) {
        if (userId <= 0) {
            String message = "Incorrect user id";
            log.debug(message);
            throw new NotFoundException(message);
        }
        if (otherUserId <= 0) {
            String message = "Incorrect other user's id";
            log.debug(message);
            throw new NotFoundException(message);
        }
        List<User> result = new ArrayList<>();
        for (Long friendId : userStorage.findUserById(userId).getFriendsIds()) {
            if (userStorage.findUserById(otherUserId).getFriendsIds().contains(friendId)) {
                result.add(userStorage.findUserById(friendId));
            }
        }
        return result;
    }
}
