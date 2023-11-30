package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        String sqlForFilmGenres = "INSERT INTO film_genres (film_id, genre_id) " +
                "VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlForFilmGenres,
                    film.getId(),
                    genre.getId());
        }
    }

    @Override
    public void update(Film film) {
        String sql = "UPDATE films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE id = ?";
        String sqlForFilmGenres = "INSERT INTO film_genres (film_id, genre_id) " +
                "VALUES (?, ?)";
        String sqlDelete = "DELETE FROM film_genres WHERE film_id = ?";
        int count = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (count == 0) {
            throw new IncorrectParamException("Неверный id!");
        }
        jdbcTemplate.update(sqlDelete, film.getId());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sqlForFilmGenres,
                    film.getId(),
                    genre.getId());
        }
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.name AS mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film getByIdFilm(long id) {
        String sql = "SELECT f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.name AS mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.id " +
                "WHERE f.id = ?";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), id);
        if (films.isEmpty()) {
            throw new IncorrectParamException("Неверный id!");
        }
        return films.get(0);
    }

    @Override
    public void addLike(long idFilm, long idUser) {
        String sql = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, idFilm, idUser);
    }

    @Override
    public void deleteLike(long idFilm, long idUser) {
        String sql = "DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?";
        int count = jdbcTemplate.update(sql, idFilm, idUser);
        if (count == 0) {
            throw new IncorrectParamException("Невереный id!");
        }
    }

    /**
     * метод определяет фильмы с максимальным количеством лайков среди всех фильмов (genreId == null И
     * year == null), среди фильмов одного года (genreId == null), среди фильмов одного жанра (year == null),
     * либо среди фильмов определенного жанра и года выпуска.
     * при введении неверного года возвращается пустой список
     * при введении неверного id жанра возвращается пустой список
     *
     * @param count   максимальеая длина возвращаемого списка
     * @param genreId id жанра по которому ведется поиск
     * @param year    год выхода фильма в прокат
     * @return возвращает список объектов класса Film
     */
    @Override
    public List<Film> getPopularFilms(int count, Integer genreId, Integer year) {

        String yearString;
        String sql;
        String genreString;
        // создаем заготовку для запроса в БД
        String sql1 = "SELECT f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.name AS mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.id " +
                "LEFT JOIN likes AS l ON f.id = l.film_id ";

        String sql2 = "GROUP BY f.id " +
                "ORDER BY COUNT(l.user_id) DESC, f.id " +
                "LIMIT ?";

        if (genreId == null) {
            if (year == null) {
                sql = sql1 + sql2;
                return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
            } else {
                yearString = "WHERE EXTRACT(YEAR FROM f.release_date) = ? ";
                sql = sql1 + yearString + sql2;
                return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), year, count);
            }
        } else {
            if (year == null) {
                genreString = "LEFT JOIN film_genres AS fg ON f.id = fg.film_id " +
                        "WHERE fg.genre_id = ? ";
                sql = sql1 + genreString + sql2;
                return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), genreId, count);
            } else {
                genreString = "LEFT JOIN film_genres AS fg ON f.id = fg.film_id " +
                        "WHERE fg.genre_id = ? AND EXTRACT(YEAR FROM f.release_date) = ? ";
                sql = sql1 + genreString + sql2;
                return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), genreId, year, count);
            }
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getLong("mpa_id"), rs.getString("mpa_name")))
                .build();
        for (Genre genre : getGenresForFilm(film.getId())) {
            film.getGenres().add(genre);
        }
        for (Long like : getLikesForFilm(film.getId())) {
            film.getLikes().add(like);
        }
        return film;
    }

    private Set<Genre> getGenresForFilm(long filmId) {
        String sql = "SELECT fg.genre_id, g.name " +
                "FROM film_genres AS fg " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.id " +
                "WHERE FILM_ID = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), filmId));
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("name"));
    }

    private Set<Long> getLikesForFilm(long filmId) {
        String sql = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeLike(rs), filmId));
    }

    private Long makeLike(ResultSet rs) throws SQLException {
        return rs.getLong("user_id");
    }
}
