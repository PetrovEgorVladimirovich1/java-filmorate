package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private long id;

    @Email
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();

    public void addFriend(long idFriend) {
        friends.add(idFriend);
    }

    public void deleteFriend(long idFriend) {
        friends.remove(idFriend);
    }
}
