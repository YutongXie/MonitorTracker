package com.huitong.monitortracker.dao.rowmapper;

import com.huitong.monitortracker.entity.MonitorTrackerJobConfigs;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MonitorTrackerJobConfigRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        MonitorTrackerJobConfigs config = new MonitorTrackerJobConfigs();
        config.setJobId(resultSet.getLong("JOB_ID"));
        config.setJobName(resultSet.getString("JOB_NAME"));
        config.setInputProcessor(resultSet.getString("INPUT_PROCESSOR"));
        config.setBusinessProcessor(resultSet.getString("BUSINESS_PROCESSOR"));
        config.setOutputProcessor(resultSet.getString("OUTPUT_PROCESSOR"));
        config.setAlertProcessor(resultSet.getString("ALERT_PROCESSOR"));
        config.setCreateTime(resultSet.getTimestamp("CREATE_TIME").toLocalDateTime());
        if(resultSet.getTimestamp("LAST_UPDATE_TIME") != null) {
            config.setLastUpdateTime(resultSet.getTimestamp("LAST_UPDATE_TIME").toLocalDateTime());
        }
        return config;
    }
}
