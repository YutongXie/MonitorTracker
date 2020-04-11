package com.huitong.monitortracker.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "jobConfigDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.jobconfig")
    public DataSource jobConfigDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "inputProcessorDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.inputprocessor")
    public DataSource inputProcessorDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "businessProcessorDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.businessprocessor")
    public DataSource businessProcessorDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "outputProcessorDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.outputprocessor")
    public DataSource outputProcessorDataSource() {
        return DataSourceBuilder.create().build();
    }
}
