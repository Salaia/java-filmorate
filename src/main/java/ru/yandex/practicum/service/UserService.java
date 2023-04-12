package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.UserDao;
import ru.yandex.practicum.model.User;

import java.util.*;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserService {
    final UserDao userDao;

    public User addFriend(Long userId, Long friendId) {
        userDao.checkUserExistence(userId);
        userDao.checkUserExistence(friendId);

        userDao.addFriend(userId, friendId);
        log.info("Users " + userId +
                " and " + friendId +
                " are friends now!");
        return userDao.findUserById(friendId);
    }

    public User removeFriend(Long userId, Long friendId) {
        userDao.checkUserExistence(userId);
        userDao.checkUserExistence(friendId);
        userDao.removeFriend(userId, friendId);
        log.info("Users " + userId +
                " and " + friendId +
                " are not friends anymore!");
        return userDao.findUserById(friendId);
    }

    public List<User> findFriends(Long userId) {
        userDao.checkUserExistence(userId);
        return userDao.findFriends(userId);
    }

    public List<User> findCommonFriends(Long userId, Long otherUserId) {
        return userDao.findCommonFriends(userId, otherUserId);
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("User was successfully saved under login: " + user.getLogin());
        return userDao.create(user);
    }

    public User update(User user) {
        userDao.checkUserExistence(user.getId()); // NotFoundException
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("User was successfully updated under login: " + user.getLogin());
        return userDao.update(user);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findUserById(Long id) {
        return userDao.findUserById(id);
    }
}
