package ru.yandex.practicum.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDBStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        String sql = "select * from filmorate.mpa_rating";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Mpa findMpaById(Long id) {
        final String sql = "select * from filmorate.mpa_rating where mpa_rating_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
    }

    @Override
    public void checkMpaExistence(Long id) {
        final String sql = "select COUNT(mpa.mpa_rating_id), " +
                "from filmorate.mpa_rating as mpa " +
                "where mpa.mpa_rating_id = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null || count == 0) {
            throw new NotFoundException("Genre with id \"" + id + "\" not found.");
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("mpa_rating_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
