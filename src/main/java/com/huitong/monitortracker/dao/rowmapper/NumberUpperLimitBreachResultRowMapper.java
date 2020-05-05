package com.huitong.monitortracker.dao.rowmapper;

import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NumberUpperLimitBreachResultRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        NumberUpperLimitBreachResult result = new NumberUpperLimitBreachResult();
        result.setSchemaName(resultSet.getString("SCHEMA_NAME"));
        result.setTableName(resultSet.getString("TABLE_NAME"));
        result.setColumnName(resultSet.getString("COLUMN_NAME"));
        result.setColumnDataType(resultSet.getString("COLUMN_DATA_TYPE"));
        result.setActive(resultSet.getString("ACTIVE"));
        result.setCurrentValue(resultSet.getBigDecimal("CURRENT_VALUE"));
        result.setLimitValue(resultSet.getString("LIMIT_VALUE"));
        result.setCreateTime(resultSet.getTimestamp("CREATE_TIME").toLocalDateTime());
        if(resultSet.getTimestamp("UPDATE_TIME") != null)
            result.setUpdateTime(resultSet.getTimestamp("UPDATE_TIME").toLocalDateTime());
        return result;
    }
}
