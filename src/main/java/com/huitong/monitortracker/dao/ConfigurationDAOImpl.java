package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.dao.rowmapper.MonitorTrackerJobConfigRowMapper;
import com.huitong.monitortracker.dao.rowmapper.MonitorTrackerJobDetailConfigRowMapper;
import com.huitong.monitortracker.entity.MonitorTrackerJobConfigs;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConfigurationDAOImpl implements ConfigurationDAO {
    @Autowired
    @Qualifier("jobConfigJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Value("${sql.job_config.parent}")
    private String sql_job_config_parent;

    @Value("${sql.job_config.detail}")
    private String sql_job_config_detail;

    @Override
    public List<MonitorTrackerJobConfigs> getJobConfig() {
        return jdbcTemplate.query(sql_job_config_parent, new MonitorTrackerJobConfigRowMapper());
    }

    @Override
    public List<MonitorTrackerJobDetailConfig> getJobDetailConfig(long jobId) {
        return jdbcTemplate.query(sql_job_config_detail,new Object[]{jobId}, new MonitorTrackerJobDetailConfigRowMapper());
    }
}
