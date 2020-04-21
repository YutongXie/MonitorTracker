package com.huitong.monitortracker.processor;

import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;

public interface BusinessProcessor {
    void execute(MonitorTrackerJobDetailConfig config);
}
