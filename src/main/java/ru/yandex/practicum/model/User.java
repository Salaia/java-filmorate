package ru.yandex.practicum.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Long id;

    @Email(message = "Invalid email!")
    String email;

    @Pattern(regexp = "[^ ]*", message = "There must be no space symbols in login!")
    @NotEmpty(message = "Login may not be empty!")
    String login;

    String name; // имя для отображения

    @Past(message = "Birth date may not be in future!")
    LocalDate birthday;

    final Set<Long> friendsIds = new HashSet<>();
}
