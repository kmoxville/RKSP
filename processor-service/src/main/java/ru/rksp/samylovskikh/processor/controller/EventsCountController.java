package ru.rksp.samylovskikh.processor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rksp.samylovskikh.processor.service.EventsCountService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Events count", description = "Подсчёт записей и запись агрегата в ClickHouse")
public class EventsCountController {

    private final EventsCountService eventsCountService;

    public EventsCountController(EventsCountService eventsCountService) {
        this.eventsCountService = eventsCountService;
    }

    @PostMapping("/events/count")
    @Operation(summary = "Получить количество записей в PostgreSQL и записать агрегат в ClickHouse")
    public ResponseEntity<Map<String, Object>> eventsCount() {
        long count = eventsCountService.countAndPersistToClickHouse();
        return ResponseEntity.ok(Map.of("count", count));
    }
}
