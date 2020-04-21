package com.huitong.monitortracker.processor;

import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;

public interface OutputProcessor {
    void execute(MonitorTrackerJobDetailConfig config);
}
