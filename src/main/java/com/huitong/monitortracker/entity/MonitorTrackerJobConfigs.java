package com.huitong.monitortracker.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
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

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getInputProcessor() {
        return inputProcessor;
    }

    public void setInputProcessor(String inputProcessor) {
        this.inputProcessor = inputProcessor;
    }

    public String getBusinessProcessor() {
        return businessProcessor;
    }

    public void setBusinessProcessor(String businessProcessor) {
        this.businessProcessor = businessProcessor;
    }

    public String getOutputProcessor() {
        return outputProcessor;
    }

    public void setOutputProcessor(String outputProcessor) {
        this.outputProcessor = outputProcessor;
    }

    public String getAlertProcessor() {
        return alertProcessor;
    }

    public void setAlertProcessor(String alertProcessor) {
        this.alertProcessor = alertProcessor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
