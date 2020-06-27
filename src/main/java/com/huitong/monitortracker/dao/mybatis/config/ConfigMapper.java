package com.huitong.monitortracker.dao.mybatis.config;

import com.huitong.monitortracker.dao.mybatis.MonitorTrackerSelectSqlProvider;
import com.huitong.monitortracker.entity.MonitorTrackerJobConfigs;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface ConfigMapper {
//    @Select("SELECT * from fly.MONITOR_TRACKER_JOB_CONFIG")
    @SelectProvider(type = MonitorTrackerSelectSqlProvider.class, method = "getJobConfigSql")
    @Results({
            @Result(column = "JOB_ID", property = "jobId"),
            @Result(column = "JOB_NAME", property = "jobName"),
            @Result(column = "JOB_DESC", property = "jobDesc"),
            @Result(column = "INPUT_PROCESSOR", property = "inputProcessor"),
            @Result(column = "BUSINESS_PROCESSOR", property = "businessProcessor"),
            @Result(column = "OUTPUT_PROCESSOR", property = "outputProcessor"),
            @Result(column = "ALERT_PROCESSOR", property = "alertProcessor"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATE_TIME", property = "createTime"),
            @Result(column = "LAST_UPDATE_TIME", property = "lastUpdateTime")
    })
    List<MonitorTrackerJobConfigs> getJobConfig();

    @SelectProvider(type = MonitorTrackerSelectSqlProvider.class, method = "getJobDetailConfigSql")
    @Results({
            @Result(column = "JOB_ID", property = "jobId"),
            @Result(column = "PROCESS_NAME", property = "processorName"),
            @Result(column = "TYPE", property = "type"),
            @Result(column = "VALUE1", property = "value1"),
            @Result(column = "VALUE2", property = "value2"),
            @Result(column = "VALUE3", property = "value3"),
            @Result(column = "VALUE4", property = "value4"),
    })
    List<MonitorTrackerJobDetailConfig> getJobDetailConfig(@Param("jobId") long jobId);
}
