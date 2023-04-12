package ru.yandex.practicum.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.GenreDao;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String sql = "select * from filmorate.genre";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    @Override
    public Genre findGenreById(Long id) {
        final String sql = "select * from filmorate.genre where genre_id = ?";

        final List<Genre> genres = jdbcTemplate.query(sql, this::mapRowToGenre, id);

        if (genres.size() == 0) {
            throw new NotFoundException("Genre not found.");
        } else {
            return genres.get(0);
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
