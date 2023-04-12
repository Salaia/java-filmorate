package ru.yandex.practicum.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.validator.IsAfterCinemaBirthday;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    @Positive
    Long id;

    @NotBlank(message = "Film name may not be only space symbols!")
    @NotEmpty(message = "Film name may not be empty!")
    String name;

    @Size(max = 200, message = "Film description max length is 200 symbols.")
    String description;

    @IsAfterCinemaBirthday(message = "Release date may not be before 1895, December, 28th.")
    LocalDate releaseDate;

    @Positive(message = "Film duration has to be positive!")
    int duration;

    final Set<Long> likes = new HashSet<>();
}
