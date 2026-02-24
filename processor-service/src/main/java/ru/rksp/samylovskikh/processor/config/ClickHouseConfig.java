package ru.rksp.samylovskikh.processor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ClickHouseConfig {

    @Value("${clickhouse.url}")
    private String url;

    @Value("${clickhouse.username:default}")
    private String username;

    @Value("${clickhouse.password:}")
    private String password;

    @Bean("clickhouseDataSource")
    public DataSource clickhouseDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.clickhouse.jdbc.ClickHouseDriver")
                .url(url)
                .username(username)
                .password(password != null ? password : "")
                .build();
    }

    @Bean("clickhouseJdbcTemplate")
    public JdbcTemplate clickhouseJdbcTemplate(DataSource clickhouseDataSource) {
        return new JdbcTemplate(clickhouseDataSource);
    }
}
