package ru.yandex.practicum.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    static UserController controller;

    @BeforeEach
    void init() {
        controller = new UserController();
    }

    @Test
    void createUserSuccess() {
        User user = new User();
        user.setId(1L);
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
        user.setId(1L);
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("  ");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.create(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Электронная почта не может быть пустой и должна содержать символ @");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void noDogEmailFailCreate() {
        User user = new User();
        user.setId(1L);
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("memail");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.create(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Электронная почта не может быть пустой и должна содержать символ @");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void spacesLoginFailCreate() {
        User user = new User();
        user.setId(1L);
        user.setName("HopeHeavens");
        user.setLogin(" S a l a i a ");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.create(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Логин не может быть пустым и содержать пробелы");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wasBornInFutureFailCreate() { // was born in future - написала и аж захотела фантастический рассказ под таким названием написать )))
        User user = new User();
        user.setId(1L);
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(3988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.create(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата рождения не может быть в будущем");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void blankNameCreateSuccess() {
        User user = new User();
        user.setId(1L);
        user.setName(" ");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        User user2 = new User();
        user2.setId(1L);
        user2.setName("Salaia");
        user2.setLogin("Salaia");
        user2.setEmail("puma.hope@yandex.ru");
        user2.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));


        assertFalse(controller.findAll().contains(user));
        assertEquals(0, controller.findAll().size());
        controller.create(user);
        assertTrue(controller.findAll().contains(user));
        assertEquals(1, controller.findAll().size());
        controller.create(user2);
        assertEquals(1, controller.findAll().size());
    }

    @Test
    void updateUserSuccess() {
        User user = new User();
        user.setId(1L);
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        assertFalse(controller.findAll().contains(user));
        controller.update(user);
        assertTrue(controller.findAll().contains(user));
    }

    @Test
    void blankEmailFailUpdate() {
        User user = new User();
        user.setId(1L);
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("  ");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.update(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Электронная почта не может быть пустой и должна содержать символ @");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void noDogEmailFailUpdate() {
        User user = new User();
        user.setId(1L);
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("memail");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.update(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Электронная почта не может быть пустой и должна содержать символ @");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void spacesLoginFailUpdate() {
        User user = new User();
        user.setId(1L);
        user.setName("HopeHeavens");
        user.setLogin(" S a l a i a ");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.update(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Логин не может быть пустым и содержать пробелы");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void wasBornInFutureFailUpdate() { // was born in future - написала и аж захотела фантастический рассказ под таким названием написать )))
        User user = new User();
        user.setId(1L);
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(3988, Month.JANUARY, 5));

        boolean hasException = false;
        try {
            controller.update(user);
        } catch (ValidationException e) {
            assertEquals(e.getMessage(), "Дата рождения не может быть в будущем");
            hasException = true;
        }
        assertTrue(hasException);
    }

    @Test
    void blankNameUpdateSuccess() {
        User user = new User();
        user.setId(1L);
        user.setName(" ");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));

        User user2 = new User();
        user2.setId(1L);
        user2.setName("Salaia");
        user2.setLogin("Salaia");
        user2.setEmail("puma.hope@yandex.ru");
        user2.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));


        assertFalse(controller.findAll().contains(user));
        assertEquals(0, controller.findAll().size());
        controller.update(user);
        assertTrue(controller.findAll().contains(user));
        assertEquals(1, controller.findAll().size());
        controller.update(user2);
        assertEquals(1, controller.findAll().size());
    }
}