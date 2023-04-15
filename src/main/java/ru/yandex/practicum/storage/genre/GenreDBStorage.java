package ru.yandex.practicum.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDBStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        String sql = "select * from filmorate.genre";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    @Override
    public Genre findGenreById(Long id) {
        final String sql = "select * from filmorate.genre where genre_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    @Override
    public void checkGenreExistence(Long id) {
        final String sql = "select COUNT(g.genre_id), " +
                "from filmorate.genre as g " +
                "where g.genre_id = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null || count == 0) {
            throw new NotFoundException("Genre with id \"" + id + "\" not found.");
        }
    }
}
