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
import java.util.stream.Collectors;

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

    @Override
    public List<Film> getPopularFilms(int count) {
        return getFilms().stream()
                .sorted(Comparator.comparingInt(f0 -> f0.getLikes().size() * -1))
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Метод возвращает список фильмов которые не лайкнул userId, но лайкнули юзеры с походим набором лайков
     * валидность параметра не проверяется, юзер с userId должен существовать
     *
     * @param userId
     * @return список объектов класса Film
     */
    @Override
    public List<Film> getUserRecommendations(Integer userId) {
        // создаем запрос для определения десяти юзеров с похожим набором лайков
        String sql = "SELECT l.user_id " +
                "FROM likes as l " +
                "WHERE l.film_id IN " +
                //вспомогательная таблица для определения лайков userId
                "(SELECT film_id " +
                "FROM likes l1 " +
                "WHERE user_id = ?) AND l.user_id <> ?" +
                "GROUP BY l.user_id " +
                //сортируем по количеству совпадений
                "ORDER BY COUNT(l.film_id) " +
                "limit 10";

        // находим id юзеров в похожим набором лайков отсортированный по похожести
        final List<Integer> similarUserIds = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), userId, userId);

        // если пользователей c похожими лайками не нашлось возвращаем пустой список фильмов
        if (similarUserIds.isEmpty()) {
            return new ArrayList<>();
        }
        // создаем универсальный запрос для нахождения списка фильмов которые лайкнул юзер
        String sqlFilm = "SELECT f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.name AS mpa_name " +
                "FROM likes AS l LEFT JOIN films AS f ON l.film_id = f.id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.id " +
                "WHERE l.user_id = ?";
        // находим список фильмов которые лайкнул userId
        List<Film> listFilmsWhichLikeUserId = jdbcTemplate.query(sqlFilm, (rs, rowNum) -> makeFilm(rs), userId);

        // проходим по найденому ранее найденому списку юзеров
        for (Integer similarUserId : similarUserIds) {
            // находим список фильмов которые лайкнул похожий юзер
            List<Film> listFilmsWhichLikeSimilarUser = jdbcTemplate.query(sqlFilm, (rs, rowNum) ->
                    makeFilm(rs), similarUserId);
            // находими различия в списках лайкнутых фильмов
            listFilmsWhichLikeSimilarUser.removeAll(listFilmsWhichLikeUserId);
            // если список не пустой, то возвращаем ответ
            if (!(listFilmsWhichLikeSimilarUser.isEmpty())) return listFilmsWhichLikeSimilarUser;
        }
        //если различий не нашлось возвращаем пустой список
        return new ArrayList<>();
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
