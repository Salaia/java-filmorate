package ru.yandex.practicum;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.UserService;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserStorageTest {

    private final UserService userService;
    private final MockMvc mockMvc;
    static final ObjectMapper objectMapper =
            new ObjectMapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    static User puma;
    static User tiger;
    static User lion;

    public static String asJsonString(final Object obj) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initCreateUserPuma() {
        puma = User.builder()
                .name("HopeHeavens")
                .login("Salaia")
                .email("puma.hope@yandex.ru")
                .birthday(LocalDate.of(1988, Month.JANUARY, 5))
                .build();
    }

    public void initCreateUserTiger() {
        tiger = User.builder()
                .name("Stripes")
                .login("Tiger")
                .email("tiger@meow.ru")
                .birthday(LocalDate.of(1988, Month.MARCH, 28))
                .build();
    }

    public void initCreateUserLion() {
        lion = User.builder()
                .name("WhiteMane")
                .login("Kovu")
                .email("uzver@roar.ru")
                .birthday(LocalDate.of(1983, Month.AUGUST, 3))
                .build();
    }

    public void initMockPerformUsersPumaOk() throws Exception {
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(puma)))
                .andExpect(status()
                        .isOk());
    }

    public void initMockPerformUsersTigerOk() throws Exception {
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(tiger)))
                .andExpect(status()
                        .isOk());
    }

    public void initMockPerformUsersLionOk() throws Exception {
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(lion)))
                .andExpect(status()
                        .isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void createUserSuccess() throws Exception {
        initCreateUserPuma();
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(puma)))
                .andExpect(status()
                        .isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void failCreateWrongObject() throws Exception {
        Film potterOne = Film.builder()
                .name("Harry Potter and the Philosopher's Stone")
                .description("The Boy Who Lived")
                .releaseDate(LocalDate.of(2001, Month.NOVEMBER, 4))
                .duration(152)
                .rate(0)
                .mpa(new Mpa(2L, "PG"))
                .build();
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(potterOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findUserByIdSuccess() throws Exception {
        initCreateUserPuma();
        initMockPerformUsersPumaOk();
        puma.setId(1L);
        initCreateUserTiger();
        initMockPerformUsersTigerOk();

        User testUser = userService.findUserById(1L);

        assertEquals(puma, testUser);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void failFindUserByIdNotFound() throws Exception {
        mockMvc
                .perform(get("/users/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void spaceSymbolsInLoginFailCreate() throws Exception {
        initCreateUserPuma();
        puma.setLogin("S a l a i a");
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(puma)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void wrongEmailFailCreate() throws Exception {
        initCreateUserPuma();
        puma.setEmail("me.mail");
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(puma)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void blankEmailFailCreate() throws Exception {
        initCreateUserPuma();
        puma.setEmail("  ");
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(puma)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void birthdayInFutureFailCreate() throws Exception {
        initCreateUserPuma();
        puma.setBirthday(LocalDate.of(3333, 12, 6));
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(puma)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void updateUserSuccess() throws Exception {
        initCreateUserPuma();
        initMockPerformUsersPumaOk();

        puma.setId(1L);
        puma.setName("Hope Updated");
        puma.setLogin("SalaiaUpdated");
        puma.setEmail("updated@yandex.ru");
        puma.setBirthday(LocalDate.of(2023, 01, 01));

        mockMvc
                .perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(puma)))
                .andExpect(status()
                        .isOk());

        User testUser = userService.findUserById(1L);
        assertEquals(puma, testUser);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void failUpdateUser() throws Exception {
        initCreateUserPuma();
        initMockPerformUsersPumaOk();

        puma.setId(10L);
        mockMvc
                .perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(puma)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findAllUsersEmptyListSuccess() {
        assertEquals(new ArrayList<>(), userService.findAll());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findAllUsersWithOneUserSuccess() throws Exception {
        initCreateUserPuma();
        initMockPerformUsersPumaOk();
        puma.setId(1L);
        List<User> checkList = new ArrayList<>();
        checkList.add(puma);
        assertEquals(checkList, userService.findAll());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void addFriendSuccess() throws Exception {
        initCreateUserPuma();
        initCreateUserTiger();
        initMockPerformUsersPumaOk();
        initMockPerformUsersTigerOk();
        puma.setId(1L);
        tiger.setId(2L);

        mockMvc
                .perform(put("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(puma)))
                .andExpect(status()
                        .isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void failAddFriendNotFound() throws Exception {
        initCreateUserPuma();
        initMockPerformUsersPumaOk();
        puma.setId(1L);

        mockMvc
                .perform(put("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(puma)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findFriendsEmptyListSuccess() throws Exception {
        initCreateUserPuma();
        initMockPerformUsersPumaOk();
        puma.setId(1L);
        assertEquals(new ArrayList<>(), userService.findFriends(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findFriendsFilledListSuccess() throws Exception {
        initCreateUserPuma();
        initCreateUserTiger();
        initCreateUserLion();
        initMockPerformUsersPumaOk();
        initMockPerformUsersTigerOk();
        initMockPerformUsersLionOk();
        puma.setId(1L);
        tiger.setId(2L);
        lion.setId(3L);

        userService.addFriend(1L, 2L);
        userService.addFriend(1L, 3L);
        List<User> checkList = new ArrayList<>();
        checkList.add(tiger);
        checkList.add(lion);
        assertEquals(checkList, userService.findFriends(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void removeFriendSuccess() throws Exception {
        initCreateUserPuma();
        initCreateUserTiger();
        initCreateUserLion();
        initMockPerformUsersPumaOk();
        initMockPerformUsersTigerOk();
        initMockPerformUsersLionOk();
        puma.setId(1L);
        tiger.setId(2L);
        lion.setId(3L);

        userService.addFriend(1L, 2L);
        userService.addFriend(1L, 3L);
        List<User> checkList = new ArrayList<>();
        checkList.add(tiger);
        checkList.add(lion);
        assertEquals(checkList, userService.findFriends(1L));

        userService.removeFriend(1L, 3L);
        checkList.remove(lion);
        assertEquals(checkList, userService.findFriends(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findCommonFriendsFilledListSuccess() throws Exception {
        initCreateUserPuma();
        initCreateUserTiger();
        initCreateUserLion();
        initMockPerformUsersPumaOk();
        initMockPerformUsersTigerOk();
        initMockPerformUsersLionOk();
        puma.setId(1L);
        tiger.setId(2L);
        lion.setId(3L);

        userService.addFriend(1L, 2L);
        userService.addFriend(1L, 3L);

        userService.addFriend(2L, 1L);
        userService.addFriend(2L, 3L);

        userService.addFriend(3L, 2L);
        userService.addFriend(3L, 1L);

        List<User> checkList = new ArrayList<>();
        checkList.add(lion);
        assertEquals(checkList, userService.findCommonFriends(1L, 2L));
    }

}