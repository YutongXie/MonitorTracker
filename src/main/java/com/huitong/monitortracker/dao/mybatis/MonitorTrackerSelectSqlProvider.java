package com.huitong.monitortracker.dao.mybatis;

import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import com.huitong.monitortracker.executor.MonitorTrackerApplicationContextAware;

import java.util.List;

public class MonitorTrackerSelectSqlProvider {

    public String getJobConfigSql() {
        return MonitorTrackerApplicationContextAware.getProperty("sql.job_config.parent");
    }

    public String getJobDetailConfigSql(long jobId) {
        String sql = MonitorTrackerApplicationContextAware.getProperty("sql.job_config.detail");
        return sql.replace("?", jobId + "");
    }

    public String getLastResultSql() {
        return MonitorTrackerApplicationContextAware.getProperty("sql.outputprocessor.number_upper_limit_breach.get_last_result");
    }

    public String inactiveLastResultSql(String schemaName) {
        String sql =  MonitorTrackerApplicationContextAware.getProperty("sql.outputprocessor.number_upper_limit_breach.inactive_old_result");
        return sql.replace("?", "#{schemaName}");
    }

    public String saveSql(List<NumberUpperLimitBreachResult> resultList) {
        String sql =  MonitorTrackerApplicationContextAware.getProperty("sql.outputprocessor.number_upper_limit_breach.insert_new_result");
        //use foreach
        String script = "<foreach collection= 'resultList' item='result' open='(' end = ')' separator=','>" +
                "#{result.schemaName},#{result.tableName},#{result.columnName},#{result.columnDataType},#{result.currentValue}" +
                ",#{result.limitValue},#{result.burnRate},#{result.daysReach80Percent},'A',system </foreach>";
        return "<script>" + sql + script + "</script>";
    }

    //Orache batch update
    //INSERT ALL
    // INTO TABLE VALUES(COLUMN1, ...)
    // INTO TABLE VALUES(COLUMN1, ...)

}
