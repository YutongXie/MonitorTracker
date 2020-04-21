package com.huitong.monitortracker.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
public class MonitorTrackerJobConfigs {

    private Long jobId;
    private String jobName;
    private String inputProcessor;
    private String businessProcessor;
    private String outputProcessor;
    private String alertProcessor;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime lastUpdateTime;

}
