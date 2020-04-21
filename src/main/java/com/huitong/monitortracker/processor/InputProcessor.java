package com.huitong.monitortracker.processor;

import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;

public interface InputProcessor {
    void execute(MonitorTrackerJobDetailConfig config);
}
