# Labyrinth Backend

A backend for a web application game (Labyrinth) made with Java and Gradle.
This backend was made as a part of a group project within bachelor studies and is meant to simulate games of [Labyrinth](https://www.ravensburger.org/ca-en/discover/labyrinth/index.html).

## Dependencies

- JDK version 17 >= (JAVA_HOME environment variable included)

## How to set up? (local)

1. Clone backend repository to your (I)DE.
2. Make a copy of `_config.properties` named `config.properties` and add a portnumber you want to run the server on. (i.e 8000 or 8080)
3. Run `.\gradlew runShadow` in terminal. (within root of cloned folder)