package com.example.compute_resource_scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    @Bean("resourcesDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.resources")
    public DataSource resourcesDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("jobsDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.jobs")
    public DataSource jobsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("resourcesJdbcTemplate")
    public JdbcTemplate resourcesJdbcTemplate() {
        return new JdbcTemplate(resourcesDataSource());
    }

    @Bean("jobsJdbcTemplate")
    public JdbcTemplate jobsJdbcTemplate() {
        return new JdbcTemplate(jobsDataSource());
    }
}
