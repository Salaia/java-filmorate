package ru.yandex.practicum.storage.film;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationExceptionForFilmorate;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.Genre;
import ru.yandex.practicum.model.Mpa;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component("filmDbStorage")
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) throws ValidationExceptionForFilmorate {
        final String sqlFilm = "insert into filmorate.films(name, description, release_date, duration, mpa_rating_id, rate) " +
                "values(?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilm, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            stmt.setInt(6, film.getRate());
            return stmt;
        }, keyHolder);

        Long key = Objects.requireNonNull(keyHolder.getKey()).longValue();

        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                final String sqlGenres = "insert into filmorate.films_genre_link(film_id, genre_id) " +
                        "values(?,?);";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement((sqlGenres));
                    stmt.setInt(1, key.intValue());
                    stmt.setInt(2, genre.getId().intValue());
                    return stmt;

                });
            }
        }
        return findFilmById(key);
    }

    @Override
    public Film update(Film film) {
        // Апдейт самого фильма
        String sqlFilmUpdate = "update filmorate.films set name = ?, " +
                "description = ?, release_date = ?, duration = ?, " +
                "mpa_rating_id = ?, rate = ?" +
                " where film_id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilmUpdate);
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            stmt.setInt(6, film.getRate());
            stmt.setLong(7, film.getId());
            return stmt;
        });


        // удаление жанров этого фильма
        final String sqlGenresDelete = "delete from filmorate.films_genre_link " +
                "where film_id = " + film.getId();
        jdbcTemplate.update(sqlGenresDelete);

        // Запись полученного списка жанров
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                final String sqlGenres = "insert into filmorate.films_genre_link(film_id, genre_id) " +
                        "values(?,?);";
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement((sqlGenres));
                    stmt.setInt(1, film.getId().intValue());
                    stmt.setInt(2, genre.getId().intValue());
                    return stmt;
                });
            }
        }
        return findFilmById(film.getId());
    }

    @Override
    public List<Film> findAllFilms() {

        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate " +
                "from filmorate.films as f " +
                "left join filmorate.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filmorate.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filmorate.genre as g on fgl.genre_id = g.genre_id " +
                "left join filmorate.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "group by f.film_id ";

        List<Optional<Film>> queryResult = jdbcTemplate.query(sql, this::mapRowToFilm);
        List<Film> films = new ArrayList<>();
        for (Optional<Film> optionalFilm : queryResult) {
            optionalFilm.ifPresent(films::add);
        }
        return films;
    }

    @Override
    public Film findFilmById(Long id) {

        final String sql = "select f.film_id, f.name as film_name, f.description, f.release_date, f.duration, " +
                "m.mpa_rating_id, m.name as mpa_name, json_arrayagg(json_object(" +
                "  KEY 'id' VALUE g.genre_id," +
                "  KEY 'name' VALUE g.name" +
                ")) as genres, " +
                " COUNT(lk.user_id) as rate " +
                //" f.rate as rate " + // это неправильно - вводить руками популярность фильма, но в тесте Постман оно так
                "from filmorate.films as f " +
                "left join filmorate.mpa_rating as m on f.mpa_rating_id = m.mpa_rating_id " +
                "left join filmorate.films_genre_link as fgl on f.film_id = fgl.film_id " +
                "left join filmorate.genre as g on fgl.genre_id = g.genre_id " +
                "left join filmorate.likes_films_users_link as lk on lk.film_id = f.film_id " +
                "where f.film_id = ? " +
                "group by f.film_id ";

        Optional<Film> optionalFilm = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);

        if (optionalFilm.isEmpty()) {
            throw new NotFoundException("Film not found.");
        } else {
            return optionalFilm.get();
        }
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        String sql = "insert into filmorate.likes_films_users_link(film_id, user_id) " +
                "values(?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, filmId.intValue());
            stmt.setInt(2, userId.intValue());
            return stmt;
        });
        Film film = findFilmById(filmId);
        film.setRate(film.getRate() + 1);
        return film;
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        final String sql = "delete from filmorate.likes_films_users_link " +
                "where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);

        Film film = findFilmById(filmId);
        film.setRate(film.getRate() - 1);
        return film;
    }

    @Override
    public void checkFilmExistence(Long id) {
        final String sql = "select COUNT(f.film_id), " +
                "from filmorate.films as f " +
                "where f.film_id = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null || count == 0) {
            throw new NotFoundException("Film with id \"" + id + "\" not found.");
        }
    }

    @Override
    public List<Genre> findAllGenres() {
        String sql = "select * from filmorate.genre";
        List<Optional<Genre>> queryResult = jdbcTemplate.query(sql, this::mapRowToGenre);
        List<Genre> genreList = new ArrayList<>();
        for (Optional<Genre> optionalGenre : queryResult) {
            optionalGenre.ifPresent(genreList::add);
        }
        return genreList;
    }

    @Override
    public Genre findGenreById(Long id) {
        final String sql = "select * from filmorate.genre where genre_id = ?";
        Optional<Genre> genre = jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
        if (genre.isEmpty()) {
            throw new NotFoundException("Genre not found.");

        } else return genre.get();
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

    @Override
    public List<Mpa> findAllMpa() {
        String sql = "select * from filmorate.mpa_rating";
        List<Optional<Mpa>> optionalList = jdbcTemplate.query(sql, this::mapRowToMpa);
        List<Mpa> mpaList = new ArrayList<>();
        for (Optional<Mpa> optionalMpa : optionalList) {
            optionalMpa.ifPresent(mpaList::add);
        }
        return mpaList;
    }

    @Override
    public Mpa findMpaById(Long id) {
        final String sql = "select * from filmorate.mpa_rating where mpa_rating_id = ?";
        Optional<Mpa> mpa = jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
        if (mpa.isEmpty()) {
            throw new NotFoundException("Mpa not found.");
        } else return mpa.get();
    }

    @Override
    public void checkMpaExistence(Long id) {
        final String sql = "select COUNT(mpa.mpa_rating_id), " +
                "from filmorate.mpa_rating as mpa " +
                "where mpa.mpa_rating_id = ? ";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null || count == 0) {
            throw new NotFoundException("Mpa with id \"" + id + "\" not found.");
        }
    }

    private Optional<Film> mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getLong("mpa_rating_id"),
                        resultSet.getString("mpa_name")))
                .rate(resultSet.getInt("rate"))
                .build();

        String genresString = resultSet.getString("genres");

        final ObjectMapper objectMapper = new ObjectMapper();
        Genre[] genres = new Genre[10];
        try {
            genres = objectMapper.readValue(genresString, Genre[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Genre genre : genres) {
            if (genre != null) {
                if (genre.getId() != null)
                    film.getGenres().add(genre);
            }
        }
        return Optional.of(film);
    }

    private Optional<Genre> mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name"))
                .build();
        return Optional.of(genre);
    }

    private Optional<Mpa> mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = Mpa.builder()
                .id(resultSet.getLong("mpa_rating_id"))
                .name(resultSet.getString("name"))
                .build();
        return Optional.of(mpa);
    }

}
