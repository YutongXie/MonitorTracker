package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.dao.rowmapper.NumberUpperLimitBreachResultRowMapper;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
@Component
public class NumberUpperLimitBreachOutputProcessorDAOImpl implements NumberUpperLimitBreachOutputProcessorDAO{
    private final Logger logger = LoggerFactory.getLogger(NumberUpperLimitBreachOutputProcessorDAOImpl.class);
    @Autowired
    @Qualifier("outputProcessorJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Value("${sql.outputprocessor.number_upper_limit_breach.inactive_old_result}")
    private String sql_inactive_last_result;
    @Value("${sql.outputprocessor.number_upper_limit_breach.get_last_result}")
    private String sql_get_last_result;
    @Value("${sql.outputprocessor.number_upper_limit_breach.insert_new_result}")
    private String sql_insert_new_result;

    @Override
    public List<NumberUpperLimitBreachResult> getLastResult() {
        return jdbcTemplate.query(sql_get_last_result, new NumberUpperLimitBreachResultRowMapper());
    }

    @Override
    public void inactiveLastResult(String schemaName) {
        jdbcTemplate.update(sql_inactive_last_result, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, schemaName);
            }
        });
    }

    @Override
    public void save(List<NumberUpperLimitBreachResult> resultList) {
       jdbcTemplate.batchUpdate(sql_insert_new_result, new BatchPreparedStatementSetter() {
           @Override
           public void setValues(PreparedStatement ps, int i) throws SQLException {
               ps.setString(1, resultList.get(i).getSchemaName());
               ps.setString(2, resultList.get(i).getTableName());
               ps.setString(3, resultList.get(i).getColumnName());
               ps.setString(4, resultList.get(i).getColumnDataType());
               ps.setBigDecimal(5, resultList.get(i).getCurrentValue());
               ps.setString(6, resultList.get(i).getLimitValue());
               ps.setBigDecimal(7, resultList.get(i).getBurnRate());
               ps.setString(8, resultList.get(i).getDaysReach80Percent());
           }

           @Override
           public int getBatchSize() {
               return resultList.size();
           }
       });

    }
}
