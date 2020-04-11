package com.huitong.monitortracker.dao.rowmapper;

import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MonitorTrackerJobDetailConfigRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        MonitorTrackerJobDetailConfig detailConfig = new MonitorTrackerJobDetailConfig();
        detailConfig.setJobId(resultSet.getLong("JOB_ID"));
        detailConfig.setProcessorName(resultSet.getString("PROCESS_NAME"));
        detailConfig.setType(resultSet.getString("TYPE"));
        detailConfig.setValue(resultSet.getString("VALUE"));
        return detailConfig;
    }
}
