-- Таблица сырые_события_университета (raw_university_events)
-- идентификатор, ФИО преподавателя, дисциплина, аудитория, дата_события

CREATE TABLE IF NOT EXISTS raw_university_events (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(255),
    fio_prepodavatelya VARCHAR(500) NOT NULL,
    disciplina VARCHAR(255) NOT NULL,
    auditoriya VARCHAR(100) NOT NULL,
    data_sobytiya TIMESTAMP NOT NULL
);

-- Алиас для задания: сырые_события_университета (view с кириллическим именем не создаём для совместимости)
