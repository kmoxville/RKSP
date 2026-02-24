package ru.rksp.samylovskikh.ingest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rksp.samylovskikh.ingest.dto.EventDto;
import ru.rksp.samylovskikh.ingest.service.EventsProducerService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Events", description = "Приём событий и отправка в RabbitMQ")
public class EventsController {

    private final EventsProducerService eventsProducerService;

    public EventsController(EventsProducerService eventsProducerService) {
        this.eventsProducerService = eventsProducerService;
    }

    @PostMapping("/events")
    @Operation(summary = "Принять событие и отправить в очередь events.raw")
    public ResponseEntity<Map<String, String>> createEvent(@Valid @RequestBody EventDto event) {
        if (event.getId() == null || event.getId().isBlank()) {
            event.setId(UUID.randomUUID().toString());
        }
        eventsProducerService.sendToEventsRaw(event);
        return ResponseEntity.ok(Map.of("status", "accepted", "id", event.getId()));
    }
}
