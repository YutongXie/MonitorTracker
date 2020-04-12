package com.huitong.monitortracker.dao.rowmapper;

import com.huitong.monitortracker.entity.NumberUpperLimitBreachStatistic;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NumberUpperLimitBreachStatisticRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        NumberUpperLimitBreachStatistic statistic = new NumberUpperLimitBreachStatistic();
        statistic.setSchemaName(resultSet.getString("SCHEMA_NAME"));
        statistic.setTableName(resultSet.getString("TABLE_NAME"));
        statistic.setColumnName(resultSet.getString("COLUMN_NAME"));
        statistic.setDataType(resultSet.getString("DATA_TYPE"));
        statistic.setDataLength(resultSet.getLong("DATA_LENGTH"));
        statistic.setDataPrecision(resultSet.getLong("DATA_PRECISION"));
        return statistic;
    }
}
