package com.huitong.monitortracker.dao.mybatis;

import com.huitong.monitortracker.executor.MonitorTrackerApplicationContextAware;

public class MonitorTrackerSelectSqlProvider {

    public String getJobConfigSql() {
        return MonitorTrackerApplicationContextAware.getProperty("sql.job_config.parent");
    }
}
