# DFD-диаграмма (нотация Гейна-Сарсона)

**Система обработки событий университета**

---

## Диаграмма 1 уровня

```mermaid
flowchart TB
    subgraph External
        User[Пользователь]
    end

    subgraph Processes
        P1["1.0 Приём событий\n(ingest-service)"]
        P2["2.0 Обработка и сохранение\n(processor-service)"]
        P3["3.0 Подсчёт и агрегация\n(processor-service)"]
    end

    subgraph Storage
        D1[("D1\nRabbitMQ\nevents.raw")]
        D2[("D2\nPostgreSQL\nraw_university_events")]
        D3[("D3\nClickHouse\nevents_aggregates")]
    end

    User -->|"POST /events\nJSON события"| P1
    P1 -->|Сообщение| D1
    D1 -->|Сообщение| P2
    P2 -->|Запись| D2
    User -->|"POST /events/count"| P3
    D2 -.->|"SELECT count()"| P3
    P3 -->|Агрегат| D3
    P3 -->|"{count: N}"| User
```

**Потоки данных:**

| № | От | К | Данные |
|---|----|---|--------|
| 1 | Пользователь | 1.0 | POST /events, JSON (ФИО, дисциплина, аудитория, дата) |
| 2 | 1.0 | D1 | Сообщение в очередь RabbitMQ |
| 3 | D1 | 2.0 | Сообщение из очереди |
| 4 | 2.0 | D2 | Запись в PostgreSQL |
| 5 | Пользователь | 3.0 | POST /events/count |
| 6 | D2 | 3.0 | Запрос count в PostgreSQL |
| 7 | 3.0 | D3 | Вставка (дата, количество) в ClickHouse |
| 8 | 3.0 | Пользователь | Ответ {count} |
