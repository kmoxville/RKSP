# ERD-диаграммы (нотация Чена)

Хранилища D2 и D3 из DFD.

## Диаграмма в нотации Чена

![ERD нотация Чена](erd-chen.png)

---

## Хранилище D2: raw_university_events (PostgreSQL)

Таблица сырых событий университета.

```mermaid
erDiagram
    raw_university_events {
        bigint id PK "идентификатор"
        varchar external_id "внешний id события"
        varchar fio_prepodavatelya "ФИО преподавателя"
        varchar disciplina "дисциплина"
        varchar auditoriya "аудитория"
        timestamp data_sobytiya "дата события"
    }
```

### Атрибуты сущности raw_university_events (сырые_события_университета)

| Атрибут | Тип | Описание |
|---------|-----|----------|
| id | BIGSERIAL, PK | Идентификатор записи |
| external_id | VARCHAR(255) | Идентификатор из источника |
| fio_prepodavatelya | VARCHAR(500) | ФИО преподавателя |
| disciplina | VARCHAR(255) | Дисциплина |
| auditoriya | VARCHAR(100) | Аудитория |
| data_sobytiya | TIMESTAMP | Дата события |

---

## Хранилище D3: events_aggregates (ClickHouse)

Таблица агрегатов событий университета.

```mermaid
erDiagram
    events_aggregates {
        DateTime data_vremya_zapisi "дата и время записи"
        UInt64 kolichestvo_zapisey "количество записей"
    }
```

### Атрибуты сущности events_aggregates (агрегаты_событий_университета)

| Атрибут | Тип | Описание |
|---------|-----|----------|
| data_vremya_zapisi | DateTime | Дата и время записи агрегата |
| kolichestvo_zapisey | UInt64 | Количество записей в raw_university_events на момент записи |

---

## Логическая связь между хранилищами

Агрегаты формируются путём подсчёта записей в raw_university_events. Прямой внешний ключ отсутствует; связь реализуется в процессе 3.0 (подсчёт и агрегация).

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

---

## Нотация Чена (текстовое представление)

**Сущность RAW_UNIVERSITY_EVENTS:**
- <u>id</u> (первичный ключ)
- external_id
- fio_prepodavatelya
- disciplina
- auditoriya
- data_sobytiya

**Сущность EVENTS_AGGREGATES:**
- data_vremya_zapisi
- kolichestvo_zapisey

**Связь ПОДСЧИТЫВАЕТ (1:N):** Один снимок агрегата соответствует одному значению COUNT по raw_university_events.
