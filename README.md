# Развёртывание

**Требования:** JDK 17, Maven, Docker.

## IntelliJ IDEA

1. File → Open → выбрать корень проекта.
2. Дождаться импорта Maven.
3. При необходимости: File → Project Structure → Project → Project SDK → 17.

## Сборка

```bash
mvn clean package
```

## Запуск в Docker

```bash
docker compose up --build
```

Приложение: http://localhost:8080  
Swagger UI: http://localhost:8080/swagger-ui.html  
Проверка: `GET http://localhost:8080/api/health`

## Локальный запуск (без образа приложения)

1. Поднять только инфраструктуру (БД и RabbitMQ):

```bash
docker compose up -d postgres rabbitmq clickhouse
```

2. Запустить приложение из IDE (Run AppApplication) или:

```bash
mvn spring-boot:run
```

Подключение к localhost:5432, localhost:5672 — уже задано в `application.yml`.
