package ru.rksp.samylovskikh.processor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rksp.samylovskikh.processor.entity.RawUniversityEvent;

public interface RawUniversityEventRepository extends JpaRepository<RawUniversityEvent, Long> {
}
