package ru.rksp.samylovskikh.processor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ClickHouseSchemaInitializer {

    private static final Logger log = LoggerFactory.getLogger(ClickHouseSchemaInitializer.class);

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS events_aggregates (" +
            "data_vremya_zapisi DateTime," +
            "kolichestvo_zapisey UInt64" +
            ") ENGINE = MergeTree() ORDER BY data_vremya_zapisi";

    public ClickHouseSchemaInitializer(@Qualifier("clickhouseDataSource") DataSource clickhouseDataSource) {
        try {
            JdbcTemplate ch = new JdbcTemplate(clickhouseDataSource);
            ch.execute(CREATE_TABLE);
            log.info("ClickHouse table events_aggregates ready");
        } catch (Exception e) {
            log.warn("ClickHouse schema init skipped (table may exist or run sql/clickhouse_init.sql manually): {}", e.getMessage());
        }
    }
}
