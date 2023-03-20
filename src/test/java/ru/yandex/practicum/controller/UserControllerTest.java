package ru.yandex.practicum.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.storage.user.UserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    static UserController controller;
    static UserService service;
    static UserStorage userStorage;
    static FilmStorage filmStorage;
    static final ObjectMapper objectMapper =
            new ObjectMapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    @BeforeEach
    void init() {
        userStorage = new InMemoryUserStorage();
        filmStorage = new InMemoryFilmStorage();
        service = new UserService(userStorage);
        controller = new UserController(service);
    }

    public static String asJsonString(final Object obj) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createUserSuccess() throws Exception {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status()
                        .isOk());
    }

    @Test
    void blankEmailFailCreate() throws Exception {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("  ");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void noDogEmailFailCreate() throws Exception {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("memail");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void spacesLoginFailCreate() throws Exception {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin(" S a l a i a ");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void wasBornInFutureFailCreate() throws Exception { // was born in future - написала и аж захотела фантастический рассказ под таким названием написать )))
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(3988, Month.JANUARY, 5));
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void blankNameCreateSuccess() throws Exception {
        User user = new User();
        user.setName(" ");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status()
                        .isOk());
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
    void blankEmailFailUpdate() throws Exception {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("me@mail.com");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        user.setEmail(" ");
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void noDogEmailFailUpdate() throws Exception {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("memail@mail.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        user.setEmail("meow");
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void spacesLoginFailUpdate() throws Exception {
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        user.setLogin(" S a l a i a ");
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void wasBornInFutureFailUpdate() throws Exception { // was born in future - написала и аж захотела фантастический рассказ под таким названием написать )))
        User user = new User();
        user.setName("HopeHeavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);

        user.setBirthday(LocalDate.of(2988, Month.JANUARY, 5));
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    void blankNameUpdateSuccess() {
        User user = new User();
        user.setName("Hope Heavens");
        user.setLogin("Salaia");
        user.setEmail("puma.hope@yandex.ru");
        user.setBirthday(LocalDate.of(1988, Month.JANUARY, 5));
        controller.create(user);
        assertNotEquals(controller.findAll().get(Math.toIntExact(user.getId()) - 1).getName(),
                controller.findAll().get(Math.toIntExact(user.getId()) - 1).getLogin());

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
    void getUserByIdSuccess() {
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

}
