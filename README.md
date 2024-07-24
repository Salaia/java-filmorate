# Filmorate 🎥

Проект социальной сети, которая поможет выбрать кино на основе того, какие фильмы вы и ваши друзья смотрите и какие оценки им ставите. Пользователи могут добавляться в друзья, добавлять редактировать фильмы, оставлять отзывы и оценки, осуществлять поиск фильмов, получать рекомендации к просмотру и выборки самых популярных фильмов.
<div>
<img width="1012" alt="landing poster" src="assets/LandingPoster.png">
</div>

Монолитное приложение, способное хранить данные в памяти или в базе данных H2. CRUD и другие запросы к базе данных написаны вручную. Полученные данные обрабатываются с помощью RowMapper.

## Основная функциональность

* Регистрация и получение информации о зарегистрированных пользователях
* Добавление и удаление пользователей в список друзей
* Пользователь может получить список своих друзей, а также список друзей, общих с другим пользователем
* Добавление, обновление и удаление фильмов
* Фильмы классифицируются по жанрам и возрастным рейтингам
* Пользователи могут ставить лайки
* Получение подборки самых популярных фильмов

## Схема базы данных
<img width="1012" alt="DataBase schema" src="assets/DbSchema.png">

## Инструкция по развёртыванию ▶️
1) Склонируйте репозиторий: https://github.com/Salaia/java-filmorate.git
2) Запустите проект: src/main/java/ru/yandex/practicum/FilmorateApplication.java
3) Проект работает по адресу: http://localhost:8080

## API
* POST /films - создание фильма
* PUT /films - редактирование фильма
* GET /films - получение списка всех фильмов
* GET /films/{id} - получение фильма по id
* PUT /films/{id}/like/{userId} — поставить лайк фильму
* DELETE /films/{id}/like/{userId} — удалить лайк фильма
* GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, возвращает первые 10

* POST /users - создание пользователя
* PUT /users - редактирование пользователя
* GET /users - получение списка всех пользователей
* GET /users/{id} - получение данных о пользователе по id
* PUT /users/{id}/friends/{friendId} — добавление в друзья
* DELETE /users/{id}/friends/{friendId} — удаление из друзей
* GET /users/{id}/friends — возвращает список друзей
* GET /users/{id}/friends/common/{otherId} — возвращает список друзей, общих с другим пользователем

* GET /genres - получение списка всех жанров
* GET /genres/{id} - получение жанра по id

* GET /mpa - получение списка всех рейтингов
* GET /mpa/{id} - получение рейтинга по id

## Testing
* Postman tests collection: postman/FilmoratePostmanTestsCollection.json
* JUnit5 tests: src/test/java

## 🛠 Tech & Tools

<div>
      <img src="https://github.com/Salaia/icons/blob/main/green/Java.png?raw=true" title="Java" alt="Java" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/SPRING%20boot.png?raw=true" title="Spring Boot" alt="Spring Boot" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/SPRING%20MVC.png?raw=true" title="Spring MVC" alt="Spring MVC" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/Maven.png?raw=true" title="Apache Maven" alt="Apache Maven" height="40"/>
    <img src="https://github.com/Salaia/icons/blob/main/green/JDBC.png?raw=true" title="JDBC" alt="JDBC" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/H2.png?raw=true" title="H2" alt="H2" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/JUnit%205.png?raw=true" title="JUnit 5" alt="JUnit 5" height="40"/>
<img src="https://github.com/Salaia/icons/blob/main/green/Postman.png?raw=true" title="Postman" alt="Postman" height="40"/>
</div>


## Статус и планы по доработке проекта
На данный момент проект проверен и зачтен ревьюером. Планов по дальнейшему развитию проекта нет.

Ссылка на групповой проект: https://github.com/Stepashka37/Filmorate-Application
