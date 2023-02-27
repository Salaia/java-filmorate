package ru.yandex.practicum.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private Long id;
    private String email;
    private String login;
    private String name; // имя для отображения
    private LocalDate birthday;


}
