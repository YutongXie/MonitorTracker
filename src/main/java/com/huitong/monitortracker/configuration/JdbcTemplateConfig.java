package com.huitong.monitortracker.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConfig {

    @Bean(name = "jobConfigJdbcTemplate")
    public JdbcTemplate jobConfigJdbcTemplate(@Qualifier("jobConfigDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "inputProcessorJdbcTemplate")
    public JdbcTemplate inputProcessorJdbcTemplate(@Qualifier("inputProcessorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "businessProcessorJdbcTemplate")
    public JdbcTemplate businessProcessorJdbcTemplate(@Qualifier("businessProcessorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "outputProcessorJdbcTemplate")
    public JdbcTemplate outputProcessorJdbcTemplate(@Qualifier("outputProcessorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
