package ru.yandex.practicum.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.MpaDao;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        String sql = "select * from filmorate.mpa_rating";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Mpa findMpaById(Long id) {
        final String sql = "select * from filmorate.mpa_rating where mpa_rating_id = ?";

        final List<Mpa> ratings = jdbcTemplate.query(sql, this::mapRowToMpa, id);

        if (ratings.size() == 0) {
            throw new NotFoundException("MPA rating not found.");
        } else {
            return ratings.get(0);
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("mpa_rating_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
