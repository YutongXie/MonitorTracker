package com.huitong.monitortracker.dao.rowmapper;

import com.huitong.monitortracker.entity.NumberUpperLimitBreachMetaData;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NumberUpperLimitBreachMetaDataRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        NumberUpperLimitBreachMetaData metaData = new NumberUpperLimitBreachMetaData();
        metaData.setSchemaName(resultSet.getString("SCHEMA_NAME"));
        metaData.setTableName(resultSet.getString("TABLE_NAME"));
        metaData.setColumnName(resultSet.getString("COLUMN_NAME"));
        metaData.setDataType(resultSet.getString("DATA_TYPE"));
        metaData.setDataLength(resultSet.getLong("DATA_LENGTH"));
        metaData.setDataPrecision(resultSet.getLong("DATA_PRECISION"));
        metaData.setDataScale(resultSet.getLong("DATA_SCALE"));
        return metaData;
    }
}
