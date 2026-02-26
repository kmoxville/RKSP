# Техническое задание

**По ГОСТ 19.201-78**

Самыловских А.И., ССПО-П-ИСиТ-нетология-2

---

## 1. Предисловие

**Наименование:** Система обработки событий университета

**Сфера применения:** учёт событий учебного процесса (занятия, дисциплины, преподаватели, аудитории) и формирование агрегированной статистики для отчётности.

---

## 2. Основание для разработки

Учебный проект в рамках курса «Сертификация и стандартизация ПО». Разработка на основе лабораторных работ №1 (DFD) и №2 (ERD).

---

## 3. Цель разработки

Автоматизация учёта событий университета, накопление сырых данных в реляционной БД и формирование агрегатов в колоночной БД для аналитических запросов.

---

## 4. Требования к программе

### 4.1 Функциональные требования

- Приём событий по REST API (POST /api/v1/events)
- Отправка событий в очередь RabbitMQ (events.raw)
- Асинхронное сохранение событий в PostgreSQL (таблица raw_university_events)
- Подсчёт количества записей по запросу (POST /api/v1/events/count)
- Запись агрегатов (дата/время, количество) в ClickHouse (таблица events_aggregates)

### 4.2 Эксплуатационные требования

- Запуск через Docker Compose
- Настройка подключений через переменные окружения (URL БД, RabbitMQ, ClickHouse)

### 4.3 Технические требования

- Java 17
- Spring Boot 3.2
- PostgreSQL 16
- ClickHouse 24
- RabbitMQ 3
- Maven 3.9

---

## 5. ERD-диаграммы хранилищ

```mermaid
erDiagram
    raw_university_events ||--o{ events_aggregates : "подсчитывает"
    raw_university_events {
        bigint id PK
        varchar fio_prepodavatelya
        varchar disciplina
        varchar auditoriya
        timestamp data_sobytiya
    }
    events_aggregates {
        DateTime data_vremya_zapisi
        UInt64 kolichestvo_zapisey
    }
```

### 5.1 raw_university_events (PostgreSQL)

| Атрибут | Тип | Описание |
|---------|-----|----------|
| id | BIGSERIAL, PK | Идентификатор |
| external_id | VARCHAR(255) | Внешний id события |
| fio_prepodavatelya | VARCHAR(500) | ФИО преподавателя |
| disciplina | VARCHAR(255) | Дисциплина |
| auditoriya | VARCHAR(100) | Аудитория |
| data_sobytiya | TIMESTAMP | Дата события |

### 5.2 events_aggregates (ClickHouse)

| Атрибут | Тип | Описание |
|---------|-----|----------|
| data_vremya_zapisi | DateTime | Дата и время записи |
| kolichestvo_zapisey | UInt64 | Количество записей |

### 5.3 Связь

Агрегаты формируются подсчётом записей в raw_university_events; прямая ссылка (FK) не используется.
