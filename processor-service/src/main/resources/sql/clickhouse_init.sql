-- Таблица агрегаты_событий_университета
-- дата_и_время_записи, количество_записей

CREATE TABLE IF NOT EXISTS events_aggregates (
    data_vremya_zapisi DateTime,
    kolichestvo_zapisey UInt64
) ENGINE = MergeTree()
ORDER BY data_vremya_zapisi;
