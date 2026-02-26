# DFD-диаграммы (нотация Гейна-Сарсона)

**Система обработки событий университета**

---

## Контекстная диаграмма (уровень 0)

```mermaid
flowchart LR
    subgraph External
        User[Пользователь]
    end
    subgraph System["Система обработки событий университета"]
        Sys[ ]
    end
    User -->|Событие| Sys
    User -->|Запрос подсчёта| Sys
    Sys -->|Количество записей| User
```

| Поток | Направление | Содержание |
|-------|-------------|------------|
| Событие | Пользователь → Система | JSON с полями события (ФИО, дисциплина, аудитория, дата) |
| Запрос подсчёта | Пользователь → Система | POST /api/v1/events/count |
| Количество записей | Система → Пользователь | JSON {count: N} |

---

## Диаграмма 1 уровня

```mermaid
flowchart TB
    User[Пользователь]
    P1["1.0 Приём событий"]
    P2["2.0 Обработка и сохранение событий"]
    P3["3.0 Подсчёт и агрегация"]
    D1[("D1 events.raw")]
    D2[("D2 raw_university_events")]
    D3[("D3 events_aggregates")]

    User -->|Событие| P1
    P1 -->|Сообщение| D1
    D1 -->|Сообщение| P2
    P2 -->|Запись| D2
    User -->|Запрос подсчёта| P3
    D2 -->|COUNT| P3
    P3 -->|Агрегат| D3
    P3 -->|Количество записей| User
```

| Процесс | Входы | Выходы |
|---------|-------|--------|
| 1.0 Приём событий | Событие (REST) | D1 events.raw |
| 2.0 Обработка и сохранение | D1 events.raw | D2 raw_university_events |
| 3.0 Подсчёт и агрегация | Запрос подсчёта, D2 | D3, Количество записей |

---

## Диаграмма 2 уровня: процесс 1.0

```mermaid
flowchart LR
    Event[Событие] --> P11["1.1 Валидация"]
    P11 --> P12["1.2 Сериализация"]
    P12 --> P13["1.3 Отправка в очередь"]
    P13 --> D1[("D1 events.raw")]
```

### Мини-спецификация 1.1 Валидация
```
ВХОД: EventDto (JSON)
ВЫХОД: EventDto (валидный)
ПРОЦЕСС:
  IF id пустой THEN id := UUID()
  IF fio_prepodavatelya пусто OR disciplina пусто OR auditoriya пусто OR data_sobytiya null
    THEN REJECT (400)
  RETURN event
```

### Мини-спецификация 1.2 Сериализация
```
ВХОД: EventDto
ВЫХОД: String (JSON)
ПРОЦЕСС:
  json := ObjectMapper.writeValueAsString(event)
  RETURN json
```

### Мини-спецификация 1.3 Отправка в очередь
```
ВХОД: String (JSON)
ВЫХОД: D1
ПРОЦЕСС:
  RabbitTemplate.convertAndSend("events.raw", json)
```

---

## Диаграмма 2 уровня: процесс 2.0

```mermaid
flowchart LR
    D1[("D1 events.raw")] --> P21["2.1 Получение сообщения"]
    P21 --> P22["2.2 Парсинг"]
    P22 --> P23["2.3 Сохранение в БД"]
    P23 --> D2[("D2 raw_university_events")]
```

### Мини-спецификация 2.1 Получение сообщения
```
ВХОД: D1 (очередь events.raw)
ВЫХОД: String
ПРОЦЕСС:
  @RabbitListener(queues = "events.raw")
  ON message RECEIVED: RETURN message.getBody()
```

### Мини-спецификация 2.2 Парсинг
```
ВХОД: String (JSON)
ВЫХОД: RawUniversityEvent
ПРОЦЕСС:
  map := ObjectMapper.readValue(message, Map)
  entity.externalId := map["id"]
  entity.fioPrepodavatelya := map["fio_prepodavatelya"]
  entity.disciplina := map["disciplina"]
  entity.auditoriya := map["auditoriya"]
  entity.dataSobytiya := parse(map["data_sobytiya"])
  RETURN entity
```

### Мини-спецификация 2.3 Сохранение в БД
```
ВХОД: RawUniversityEvent
ВЫХОД: D2
ПРОЦЕСС:
  repository.save(entity)
```

---

## Диаграмма 2 уровня: процесс 3.0

```mermaid
flowchart LR
    Req[Запрос подсчёта] --> P31["3.1 Запрос count"]
    D2[("D2")] --> P31
    P31 --> P32["3.2 Формирование агрегата"]
    P32 --> P33["3.3 Запись в ClickHouse"]
    P33 --> D3[("D3 events_aggregates")]
    P31 --> Resp[Количество записей]
```

### Мини-спецификация 3.1 Запрос count
```
ВХОД: D2 (PostgreSQL)
ВЫХОД: Long, передача в 3.2 и Resp
ПРОЦЕСС:
  count := postgresRepository.count()
  RETURN count
```

### Мини-спецификация 3.2 Формирование агрегата
```
ВХОД: count (Long)
ВЫХОД: (DateTime, UInt64)
ПРОЦЕСС:
  now := LocalDateTime.now()
  RETURN (now, count)
```

### Мини-спецификация 3.3 Запись в ClickHouse
```
ВХОД: (data_vremya_zapisi, kolichestvo_zapisey)
ВЫХОД: D3
ПРОЦЕСС:
  INSERT INTO events_aggregates VALUES (now, count)
  via HTTP POST to ClickHouse
```
