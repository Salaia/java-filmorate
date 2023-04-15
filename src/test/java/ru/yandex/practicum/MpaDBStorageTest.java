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
import ru.yandex.practicum.model.Mpa;
import ru.yandex.practicum.service.MpaService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDBStorageTest {
    private final MpaService mpaService;

    private final MockMvc mockMvc;
    static List<Mpa> mpaList = new ArrayList<>();
    static final ObjectMapper objectMapper =
            new ObjectMapper().disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

    public static String asJsonString(final Object obj) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void fillInMpaList() {
        mpaList.add(new Mpa(1L, "G"));
        mpaList.add(new Mpa(2L, "PG"));
        mpaList.add(new Mpa(3L, "PG-13"));
        mpaList.add(new Mpa(4L, "R"));
        mpaList.add(new Mpa(5L, "NC-17"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findMpaByIdSuccess() throws Exception {
        Mpa mpaG = Mpa.builder()
                .id(1L)
                .name("G")
                .build();

        Mpa testMpa = mpaService.findMpaById(1L);
        assertEquals(mpaG, testMpa);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void failFindMpaByWrongId() throws Exception {
        mockMvc
                .perform(get("/mpa/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null)))
                .andExpect(status()
                        .isNotFound());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    public void findAllMpaSuccess() {
        fillInMpaList();
        assertEquals(mpaList, mpaService.findAll());
    }
}
