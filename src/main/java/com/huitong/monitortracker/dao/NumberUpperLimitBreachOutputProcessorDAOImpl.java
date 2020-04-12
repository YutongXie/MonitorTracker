package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.dao.rowmapper.NumberUpperLimitBreachResultRowMapper;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class NumberUpperLimitBreachOutputProcessorDAOImpl implements NumberUpperLimitBreachOutputProcessorDAO{
    private final Logger logger = LoggerFactory.getLogger(NumberUpperLimitBreachOutputProcessorDAOImpl.class);
    @Autowired
    @Qualifier("outputProcessorJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Value("${sql.outprocessor.number_upper_limit_breach.inactive_old_result}")
    private String sql_inactive_last_result;
    @Value("${sql.outprocessor.number_upper_limit_breach.get_last_result}")
    private String sql_get_last_result;
    @Override
    public List<NumberUpperLimitBreachResult> getLastResult() {
        return jdbcTemplate.query(sql_get_last_result, new NumberUpperLimitBreachResultRowMapper());
    }

    @Override
    public void inactiveLastResult() {
        jdbcTemplate.update(sql_inactive_last_result);
    }

    @Override
    public void save(String[] sql) {
        jdbcTemplate.batchUpdate(sql);
    }
}
