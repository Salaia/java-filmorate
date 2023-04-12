package ru.yandex.practicum.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {
    Long id;
    String name;

    public Genre() {
    }

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
