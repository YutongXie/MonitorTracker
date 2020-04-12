package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.dao.rowmapper.NumberUpperLimitBreachStatisticRowMapper;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class NumberUpperLimitBreachInputProcessorDAOImpl implements NumberUpperLimitBreachInputProcessorDAO{
    @Autowired
    @Qualifier("inputProcessorJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("${sql.inputprocessor.number_upper_limit_breach}")
    private String sql_get_statistic;
    @Override
    public List<NumberUpperLimitBreachStatistic> getStatistic() {
        return jdbcTemplate.query(sql_get_statistic, new NumberUpperLimitBreachStatisticRowMapper());
    }
}
