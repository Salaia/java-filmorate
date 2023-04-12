package ru.yandex.practicum.dao.impl;

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

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDaoImplTest {

    private final FilmDaoImpl filmDao;
    private final MockMvc mockMvc;
    static final ObjectMapper objectMapper =
            new ObjectMapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    static User userOne;
    static Film potterOne;

    public static String asJsonString(final Object obj) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void initFilmPotterOne() {
        potterOne = Film.builder()
                .name("Harry Potter and the Philosopher's Stone")
                .description("The Boy Who Lived")
                .releaseDate(LocalDate.of(2001, Month.NOVEMBER, 4))
                .duration(152)
                .rate(0)
                .mpa(new Mpa(2L, "PG"))
                .build();
    }

    public void initMockPotterOneOk() throws Exception {
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(potterOne)))
                .andExpect(status()
                        .isOk());
    }

    public void initCreateUserUserOne() {
        userOne = User.builder()
                .name("First")
                .login("userOne")
                .email("first@usermail.ru")
                .birthday(LocalDate.of(1963, Month.MAY, 27))
                .build();
    }

    public void initMockPerformUsersUserOneOk() throws Exception {
        mockMvc
                .perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userOne)))
                .andExpect(status()
                        .isOk());
    }


    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmCreateSuccess() throws Exception {
        initFilmPotterOne();
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(potterOne)))
                .andExpect(status()
                        .isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmFindByIdSuccess() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        potterOne.setId(1L);

        Film testFilm = filmDao.findFilmById(1L);
        assertEquals(potterOne, testFilm);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void failFindFilmByWrongId() throws Exception {
        mockMvc
                .perform(get("/films/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void findAllFilmsEmptyListSuccess() throws Exception {
        assertEquals(filmDao.findAll(), new ArrayList<>());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void findAllFilmsWithOneFilmOnListSuccess() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        potterOne.setId(1L);

        ArrayList<Film> checkList = new ArrayList<>();
        checkList.add(potterOne);
        assertEquals(checkList, filmDao.findAll());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void failCreateFilmEmptyName() throws Exception {
        initFilmPotterOne();
        potterOne.setName("");

        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(potterOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void failCreateFilmDescriptionTooLong() throws Exception {
        initFilmPotterOne();
        potterOne.setDescription("Too long description. Too long description. Too long description. Too long description. " +
                "Too long description. Too long description. Too long description. Too long description. Too long description. " +
                "Too long description. Too long description. Too long description. Too long description. Too long description. ");
        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(potterOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void failCreateFilmReleaseDateIsTooEarly() throws Exception {
        initFilmPotterOne();
        potterOne.setReleaseDate(LocalDate.of(1000, 1, 1));

        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(potterOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void failCreateFilmNegativeDuration() throws Exception {
        initFilmPotterOne();
        potterOne.setDuration(-152);

        mockMvc
                .perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(potterOne)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateSuccess() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        Film updatePotter = Film.builder()
                .id(1L)
                .name("Harry Potter one Updated")
                .description("The Boy Who Lived Updated")
                .releaseDate(LocalDate.of(2001, Month.NOVEMBER, 4))
                .duration(152)
                .mpa(new Mpa(2L, "PG"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePotter)))
                .andExpect(status()
                        .isOk());
        assertEquals(updatePotter, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailNegativeId() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        Film updatePotter = Film.builder()
                .id(-1L)
                .name("Harry Potter one Updated")
                .description("The Boy Who Lived Updated")
                .releaseDate(LocalDate.of(2001, Month.NOVEMBER, 4))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(2L, "PG"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePotter)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updatePotter, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailNameIsBlank() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        Film updatePotter = Film.builder()
                .id(1L)
                .name("    ")
                .description("The Boy Who Lived Updated")
                .releaseDate(LocalDate.of(2001, Month.NOVEMBER, 4))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(2L, "PG"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePotter)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updatePotter, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailNameIsEmpty() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        Film updatePotter = Film.builder()
                .id(1L)
                .name("")
                .description("The Boy Who Lived Updated")
                .releaseDate(LocalDate.of(2001, Month.NOVEMBER, 4))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(2L, "PG"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePotter)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updatePotter, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailNameIsNull() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        Film updatePotter = Film.builder()
                .id(1L)
                .name(null)
                .description("The Boy Who Lived Updated")
                .releaseDate(LocalDate.of(2001, Month.NOVEMBER, 4))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(2L, "PG"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePotter)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updatePotter, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailDescriptionTooLong() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));


        Film updatePotter = Film.builder()
                .id(1L)
                .name("Harry Potter one Updated")
                .description("Too long description. Too long description. Too long description. Too long description. " +
                        "Too long description. Too long description. Too long description. Too long description. Too long description. " +
                        "Too long description. Too long description. Too long description. Too long description. Too long description. ")
                .releaseDate(LocalDate.of(2001, Month.NOVEMBER, 4))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(2L, "PG"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePotter)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updatePotter, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailToEarlyDate() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        Film updatePotter = Film.builder()
                .id(1L)
                .name("Arrival of a Harry Potter")
                .description("The Boy Who Arrived Instead Of Train")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 27))
                .duration(152)
                .rate(1)
                .mpa(new Mpa(2L, "PG"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePotter)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updatePotter, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void filmUpdateFailNegativeDuration() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        Film updatePotter = Film.builder()
                .id(1L)
                .name("Harry Potter one Updated")
                .description("The Boy Who Lived Updated")
                .releaseDate(LocalDate.of(2001, Month.NOVEMBER, 4))
                .duration(-1)
                .rate(1)
                .mpa(new Mpa(2L, "PG"))
                .build();

        mockMvc
                .perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePotter)))
                .andExpect(status()
                        .isBadRequest());
        assertNotEquals(updatePotter, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void addLikeSuccess() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        final Film initialFilm = filmDao.findFilmById(1L);

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);

        filmDao.addLike(1L, 1L);

        assertNotEquals(likedFilm, initialFilm);
        assertEquals(likedFilm, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void addLikeFailFilmIdIsNegative() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        final Film initialFilm = filmDao.findFilmById(1L);

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);

        mockMvc
                .perform(put("/films/-1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void addLikeFailUserIdIsNegative() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        final Film initialFilm = filmDao.findFilmById(1L);

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);
        mockMvc
                .perform(put("/films/1/like/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void removeLikeSuccess() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        final Film initialFilm = filmDao.findFilmById(1L);

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);
        filmDao.addLike(1L, 1L);

        assertNotEquals(likedFilm, initialFilm);
        assertEquals(likedFilm, filmDao.findFilmById(1L));

        final Film filmWithLike = filmDao.findFilmById(1L);
        filmDao.removeLike(1L, 1L);
        final Film filmWithOutLike = filmDao.findFilmById(1L);

        assertNotEquals(filmWithLike, filmWithOutLike);
        assertEquals(initialFilm, filmDao.findFilmById(1L));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void removeLikeFailNegativeFilmId() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        final Film initialFilm = filmDao.findFilmById(1L);

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);
        filmDao.addLike(1L, 1L);

        assertNotEquals(likedFilm, initialFilm);
        assertEquals(likedFilm, filmDao.findFilmById(1L));
        mockMvc
                .perform(delete("/films/-1/like/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    void removeLikeFailNegativeUserId() throws Exception {
        initFilmPotterOne();
        initMockPotterOneOk();
        initCreateUserUserOne();
        initMockPerformUsersUserOneOk();
        potterOne.setId(1L);
        assertEquals(potterOne, filmDao.findFilmById(1L));

        final Film initialFilm = filmDao.findFilmById(1L);

        final Film likedFilm = filmDao.findFilmById(1L);
        likedFilm.setRate(1);
        filmDao.addLike(1L, 1L);

        assertNotEquals(likedFilm, initialFilm);
        assertEquals(likedFilm, filmDao.findFilmById(1L));
        mockMvc
                .perform(put("/films/1/like/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status()
                        .isBadRequest());
    }
}