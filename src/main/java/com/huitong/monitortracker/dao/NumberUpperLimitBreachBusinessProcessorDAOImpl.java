package com.huitong.monitortracker.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class NumberUpperLimitBreachBusinessProcessorDAOImpl implements NumberUpperLimitBreachBusinessProcessorDAO{
    @Autowired
    @Qualifier("businessProcessorJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public String getTableColumnCurrentValue(String sql) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, String.class)).orElse("");
    }
}
