package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.dao.rowmapper.NumberUpperLimitBreachMetaDataRowMapper;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachMetaData;
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
    private String sql_get_metadata;
    @Override
    public List<NumberUpperLimitBreachMetaData> getMetaData() {
        return jdbcTemplate.query(sql_get_metadata, new NumberUpperLimitBreachMetaDataRowMapper());
    }
}
