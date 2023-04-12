package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.*;

@RestController
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @PostMapping // POST /users
    public User create(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping // PUT /users
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping // GET /users
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}") // GET /users/{id}
    public User findUserById(@PathVariable("id") @Positive Long id) {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}") // PUT /users/{id}/friends/{friendId} — добавление в друзья.
    public User addFriend(@PathVariable("id") @Positive Long userId,
                          @PathVariable("friendId") Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}") // DELETE /users/{id}/friends/{friendId} — удаление из друзей.
    public User removeFriend(@PathVariable("id") @Positive Long userId,
                             @PathVariable("friendId") Long friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("{id}/friends") // GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
    public List<User> findFriends(@PathVariable("id") @Positive Long userId) {
        return userService.findFriends(userId);
    }

    // GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") @Positive Long userId,
                                        @PathVariable("otherId") @Positive Long otherUserId) {
        return userService.findCommonFriends(userId, otherUserId);
    }
}
