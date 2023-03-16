/*
package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    static UserController controller;
    static UserService service;
    static UserStorage storage;

    @BeforeEach
    void init() {
        storage = new InMemoryUserStorage();
        service = new UserService(storage);
        controller = new UserController(service);
    }

    @Test
    void createUserSuccess() {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        assertFalse(controller.findAll().contains(user));
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void blankEmailFailCreate() {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("  ");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.create(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Email may not be empty and has to contain '@' symbol!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void noDogEmailFailCreate() {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("memail");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.create(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Email may not be empty and has to contain '@' symbol!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void spacesLoginFailCreate() {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin(" S a l a i a ");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.create(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Login may not be empty or contain any space-symbols!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wasBornInFutureFailCreate() { // was born in future - написала и аж захотела фантастический рассказ под таким названием написать )))
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(3988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.create(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Date of birth may not be in future!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void blankNameCreateSuccess() {
        User user = new User();
        user.setName(" ");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        assertEquals(controller.findAll().get(Math.toIntExact(user.getId()) - 1).getName(),
                controller.findAll().get(Math.toIntExact(user.getId()) - 1).getLogin());
    }

    @Test
    void updateUserSuccess() {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        User user2 = new User();
        user2.setName("HopeHeavens");
        user2.setId(user.getId());
        user2.setLogin("Lessera");
        user2.setEmail("puma.hope@yandex.ru");
        user2.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.update(user2);

        assertEquals("Lessera", controller.findAll().get(Math.toIntExact(user.getId()) - 1).getLogin());
    }

    @Test
    void blankEmailFailUpdate() {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("me@mail.com");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        boolean hasException = false;
        try {
            user.setEmail(" ");
            controller.update(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Email may not be empty and has to contain '@' symbol!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void noDogEmailFailUpdate() {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("memail@mail.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        boolean hasException = false;
        try {
            user.setEmail("meow");
            controller.update(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Email may not be empty and has to contain '@' symbol!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void spacesLoginFailUpdate() {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        boolean hasException = false;
        try {
            user.setLogin(" S a l a i a ");
            controller.update(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Login may not be empty or contain any space-symbols!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wasBornInFutureFailUpdate() { // was born in future - написала и аж захотела фантастический рассказ под таким названием написать )))
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        boolean hasException = false;
        try {
            user.setBirthday(LocalDate.of(2988, Month.JANUARY, 5));
            controller.update(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Date of birth may not be in future!");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void blankNameUpdateSuccess() {
        User user = new User();
        user.setName("Hope Heavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        User user2 = new User();
        user2.setName(" ");
        user2.setId(user.getId());
        user2.setLogin("Salaia");
        user2.setEmail("puma.hope@yandex.ru");
        user2.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.update(user2);

        assertEquals(controller.findAll().get(Math.toIntExact(user.getId()) - 1).getName(),
                controller.findAll().get(Math.toIntExact(user.getId()) - 1).getLogin());

    }

    @Test
    void getUserByIdSuccess(){
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        Long id = user.getId();
        User userRestored = controller.findUserById(id);
        assertEquals(user, userRestored);
    }

    // TODO тестирование негативных
}*/
