package ru.rksp.samylovskikh.processor.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.rksp.samylovskikh.processor.entity.RawUniversityEvent;
import ru.rksp.samylovskikh.processor.repository.RawUniversityEventRepository;

import java.time.Instant;
import java.util.Map;

@Component
public class EventsRawListener {

    private final RawUniversityEventRepository repository;
    private final ObjectMapper objectMapper;

    public EventsRawListener(RawUniversityEventRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "events.raw")
    public void handleEvent(String message) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = objectMapper.readValue(message, Map.class);
            RawUniversityEvent entity = new RawUniversityEvent();
            entity.setExternalId(map.get("id") != null ? map.get("id").toString() : null);
            entity.setFioPrepodavatelya(getString(map, "fioPrepodavatelya") != null ? getString(map, "fioPrepodavatelya") : getString(map, "fio_prepodavatelya"));
            entity.setDisciplina(getString(map, "disciplina"));
            entity.setAuditoriya(getString(map, "auditoriya"));
            Object ds = map.get("dataSobytiya");
            if (ds == null) ds = map.get("data_sobytiya");
            if (ds instanceof String) {
                entity.setDataSobytiya(Instant.parse((String) ds));
            } else if (ds instanceof Number) {
                entity.setDataSobytiya(Instant.ofEpochMilli(((Number) ds).longValue()));
            } else {
                entity.setDataSobytiya(Instant.now());
            }
            repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process event: " + e.getMessage(), e);
        }
    }

    private static String getString(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return v != null ? v.toString() : null;
    }
}
