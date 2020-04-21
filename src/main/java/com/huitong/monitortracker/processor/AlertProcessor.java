package com.huitong.monitortracker.processor;

import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;

public interface AlertProcessor {
    void execute(MonitorTrackerJobDetailConfig config);
}
