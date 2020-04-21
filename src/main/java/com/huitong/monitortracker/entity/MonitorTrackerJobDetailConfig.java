package com.huitong.monitortracker.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Setter
@Getter
@ToString
public class MonitorTrackerJobDetailConfig {

    private Long jobId;
    private String processorName;
    private String type;
    private String value1;
    private String value2;
    private String value3;
    private String value4;
    private String value5;
}
