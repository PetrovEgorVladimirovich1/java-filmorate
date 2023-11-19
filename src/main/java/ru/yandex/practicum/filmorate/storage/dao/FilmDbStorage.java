package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParamException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FilmDbStorage implements FilmStorage {
    private long id = 0;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private long addId() {
        return ++id;
    }

    @Override
    public void create(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        String sqlForFilmGenres = "INSERT INTO film_genres (film_id, genre_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(addId());
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
                "LEFT JOIN mpa AS m ON f.mpa_id = m.id ";
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
        if (films.size() != 1) {
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

    @Override
    public List<Film> getPopularFilms(int count) {
        return getFilms().stream()
                .sorted(Comparator.comparingInt(f0 -> f0.getLikes().size() * -1))
                .limit(count)
                .collect(Collectors.toList());
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
