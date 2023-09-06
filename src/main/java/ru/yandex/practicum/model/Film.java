package ru.yandex.practicum.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.validator.IsAfterCinemaBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    @Positive
    Long id;

    @NotBlank(message = "Film name may not be only space symbols!")
    @NotEmpty(message = "Film name may not be empty!")
    String name;

    @Size(max = 200, message = "Film description max length is 200 symbols.")
    String description;

    @IsAfterCinemaBirthday
    LocalDate releaseDate;

    @Positive(message = "Film duration has to be positive!")
    int duration;

    final LinkedHashSet<Genre> genres = new LinkedHashSet<>();

    Mpa mpa;
    int rate;

    final Set<Long> likes = new HashSet<>();
}
