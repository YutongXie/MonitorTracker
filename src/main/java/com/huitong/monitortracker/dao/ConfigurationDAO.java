package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.entity.MonitorTrackerJobConfigs;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;

import java.util.List;

public interface ConfigurationDAO {
    List<MonitorTrackerJobConfigs> getJobConfig();
    List<MonitorTrackerJobDetailConfig> getJobDetailConfig(long jobId);
}
