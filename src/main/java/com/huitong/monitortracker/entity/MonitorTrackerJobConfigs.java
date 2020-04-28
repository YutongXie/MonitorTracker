package com.huitong.monitortracker.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@Entity
@Table(name="MONITOR_TRACKER_JOB_CONFIG", schema = "COMMON")
public class MonitorTrackerJobConfigs {
    @Id
    @GeneratedValue
    @Column(name = "JOB_ID")
    private Long jobId;
    @Column(name = "JOB_NAME")
    private String jobName;
    @Column(name = "INPUT_PROCESSOR")
    private String inputProcessor;
    @Column(name = "BUSINESS_PROCESSOR")
    private String businessProcessor;
    @Column(name = "OUTPUT_PROCESSOR")
    private String outputProcessor;
    @Column(name = "ALERT_PROCESSOR")
    private String alertProcessor;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;
    @Column(name = "LAST_UPDATE_TIME")
    private LocalDateTime lastUpdateTime;

}
