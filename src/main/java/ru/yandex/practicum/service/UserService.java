package ru.yandex.practicum.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.UserStorage;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
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
    public User addFriend(@Positive Long userId, @Positive Long friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);

        user.getFriendsIds().add(friendId);
        friend.getFriendsIds().add(userId);
        log.info("Users " + userStorage.findUserById(userId).getName() +
                " and " + userStorage.findUserById(friendId).getName() +
                " are friends now!");
        return userStorage.findUserById(friendId);
    }

    public User removeFriend(@Positive Long userId, @Positive Long friendId) {
        userStorage.findUserById(userId).getFriendsIds().remove(friendId);
        userStorage.findUserById(friendId).getFriendsIds().remove(userId);
        log.info("Users " + userStorage.findUserById(userId).getName() +
                " and " + userStorage.findUserById(friendId).getName() +
                " are not friends anymore!");
        return userStorage.findUserById(friendId);
    }

    public List<User> findFriends(@Positive Long userId) {
        userStorage.findUserById(userId);
        return userStorage.findAll().stream()
                .filter(user -> user.getFriendsIds().contains(userId))
                .collect(Collectors.toList());
        /*List<User> result = new ArrayList<>();
        for (Long friendId : userStorage.findUserById(userId).getFriendsIds()) {
            result.add(userStorage.findUserById(friendId));
        }
        return result;*/
    }

    public List<User> findCommonFriends(@Positive Long userId, @Positive Long otherUserId) {
        User user = userStorage.findUserById(userId);
        User otherUser = userStorage.findUserById(otherUserId);
        Set<Long> userFriends = user.getFriendsIds();
        Set<Long> otherUserFriends = otherUser.getFriendsIds();
        Set<Long> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherUserFriends);

        List<User> result = new ArrayList<>();
        for (Long friendId : commonFriends) {
            result.add(userStorage.findUserById(friendId));
        }
        return result;
    }

    public User create(@Valid User user) {
        return userStorage.create(user);
    }

    public User update(@Valid User user) {
        return userStorage.update(user);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findUserById(@Positive Long id) {
        return userStorage.findUserById(id);
    }
}
