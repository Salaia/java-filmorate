package ru.yandex.practicum.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationExceptionForFilmorate;
import ru.yandex.practicum.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component("userDbStorage")
@Qualifier("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) throws ValidationExceptionForFilmorate {

        String sql = "insert into filmorate.users(email, login, name, birthday) " +
                "values(?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        Long key = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return findUserById(key);
    }

    @Override
    public User update(User user) {
        String sql = "update filmorate.users set user_id = ?, email = ?, " +
                "login = ?, name = ?, birthday = ?";
        jdbcTemplate.update(sql, user.getId(), user.getEmail(),
                user.getLogin(), user.getName(), user.getBirthday());
        return findUserById(user.getId());
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from filmorate.users";
        List<Optional<User>> queryResult = jdbcTemplate.query(sql, this::mapRowToUser);
        List<User> users = new ArrayList<>();
        for (Optional<User> optionalUser : queryResult) {
            optionalUser.ifPresent(users::add);
        }
        return users;
    }

    @Override
    public User findUserById(Long id) {

        final String sql = "select * from filmorate.users where USER_ID = ?";
        Optional<User> optionalUser = jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found.");
        } else {
            return optionalUser.get();
        }
    }

    public void addFriend(Long userId, Long friendId) {
        String sql = "insert into filmorate.friendship_user_to_user_link(USER_ID, friend_id) " +
                "values(?,?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, userId);
            stmt.setLong(2, friendId);
            return stmt;
        });
    }

    @Override
    public List<User> findFriends(Long userId) {
        final String sql = "select * " +
                "from filmorate.users as u " +
                "where u.USER_ID in (select f.friend_id " +
                "from filmorate.friendship_user_to_user_link as f " +
                "where f.user_id = ?);";

        List<Optional<User>> queryResult = jdbcTemplate.query(sql, this::mapRowToUser, userId);
        List<User> users = new ArrayList<>();
        for (Optional<User> optionalUser : queryResult) {
            optionalUser.ifPresent(users::add);
        }
        return users;
    }

    public List<User> findCommonFriends(Long userId, Long otherUserId) {
        final String sql = "select * " +
                "from filmorate.users as u " +
                "where u.USER_ID in (select f.friend_id " +
                "from filmorate.friendship_user_to_user_link as f " +
                "where f.user_id = ? " +
                "and f.friend_id in (select f1.friend_id " +
                "from filmorate.friendship_user_to_user_link as f1 " +
                "where f1.user_id = ? ));";

        List<Optional<User>> queryResult = jdbcTemplate.query(sql, this::mapRowToUser, userId, otherUserId);
        List<User> users = new ArrayList<>();
        for (Optional<User> optionalUser : queryResult) {
            optionalUser.ifPresent(users::add);
        }
        return users;
    }

    public User removeFriend(Long userId, Long friendId) {
        final String sql = "delete from filmorate.friendship_user_to_user_link " +
                "where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
        return findUserById(userId);
    }

    @Override
    public void checkUserExistence(Long id) {
        final String sql = "select COUNT(USER_ID) from filmorate.users where USER_ID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        if (count == null || count == 0) {
            throw new NotFoundException("User with id \"" + id + "\" not found.");
        }
    }

    private Optional<User> mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = User.builder()
                .id(resultSet.getLong("USER_ID"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        return Optional.of(user);
    }
}
