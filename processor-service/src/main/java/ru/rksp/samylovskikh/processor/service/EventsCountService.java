package ru.rksp.samylovskikh.processor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.rksp.samylovskikh.processor.repository.RawUniversityEventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EventsCountService {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS events_aggregates (" +
            "data_vremya_zapisi DateTime," +
            "kolichestvo_zapisey UInt64" +
            ") ENGINE = MergeTree() ORDER BY data_vremya_zapisi";

    private final RawUniversityEventRepository postgresRepository;
    private final String clickhouseHttpUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public EventsCountService(RawUniversityEventRepository postgresRepository,
                              @Value("${clickhouse.http-url}") String clickhouseHttpUrl) {
        this.postgresRepository = postgresRepository;
        this.clickhouseHttpUrl = clickhouseHttpUrl.replaceAll("/$", "");
    }

    public long countAndPersistToClickHouse() {
        long count = postgresRepository.count();
        try {
            postQuery(CREATE_TABLE);
        } catch (Exception ignored) {
            // table may already exist
        }
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String insertSql = "INSERT INTO events_aggregates (data_vremya_zapisi, kolichestvo_zapisey) VALUES ('" + dateStr + "', " + count + ")";
        postQuery(insertSql);
        return count;
    }

    private void postQuery(String sql) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> entity = new HttpEntity<>(sql, headers);
        try {
            ResponseEntity<String> resp = restTemplate.postForEntity(clickhouseHttpUrl, entity, String.class);
            if (!resp.getStatusCode().is2xxSuccessful() || (resp.getBody() != null && resp.getBody().contains("Exception"))) {
                throw new IllegalStateException("ClickHouse error: " + resp.getStatusCode() + " " + resp.getBody());
            }
        } catch (org.springframework.web.client.RestClientException e) {
            throw new IllegalStateException("ClickHouse request failed: " + e.getMessage(), e);
        }
    }
}
