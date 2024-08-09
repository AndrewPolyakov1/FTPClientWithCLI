# Тестовое задание

## Сборка

Для сборки проекта в JAR файл требуется Java 8 и Gradle 8.8.

```bash
./gradlew shadowJar
```

## Запуск

```bash
java -jar build/libs/FTP_Assignment-1.0.jar <login> <password> <host> <port> <remotePath>
```

## Запуск тестов

```bash
./gradlew test 
```