package com.huitong.monitortracker.utils;

import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class MonitorTrackerConfigurationUtils {

    private final static String INPUT_PROCESSOR_INDICATOR = "INPUT_PROCESSOR";
    private final static String BUSINESS_PROCESSOR_INDICATOR = "BUSINESS_PROCESSOR";
    private final static String OUTPUT_PROCESSOR_INDICATOR = "OUTPUT_PROCESSOR";
    private final static String ALERT_PROCESSOR_INDICATOR = "ALERT_PROCESSOR";

    public static MonitorTrackerJobDetailConfig getInputProcessorJobDetailConfig(List<MonitorTrackerJobDetailConfig> jobDetailConfigList) {
        if(CollectionUtils.isEmpty(jobDetailConfigList)) {
            return null;
        }

        for (MonitorTrackerJobDetailConfig config : jobDetailConfigList) {
            if(INPUT_PROCESSOR_INDICATOR.equalsIgnoreCase(config.getProcessorName())) {
                return config;
            }
        }
        return null;
    }

    public static MonitorTrackerJobDetailConfig getBusinessProcessorJobDetailConfig(List<MonitorTrackerJobDetailConfig> jobDetailConfigList) {
        if(CollectionUtils.isEmpty(jobDetailConfigList)) {
            return null;
        }

        for (MonitorTrackerJobDetailConfig config : jobDetailConfigList) {
            if(BUSINESS_PROCESSOR_INDICATOR.equalsIgnoreCase(config.getProcessorName())) {
                return config;
            }
        }
        return null;
    }

    public static MonitorTrackerJobDetailConfig getOutputProcessorJobDetailConfig(List<MonitorTrackerJobDetailConfig> jobDetailConfigList) {
        if(CollectionUtils.isEmpty(jobDetailConfigList)) {
            return null;
        }

        for (MonitorTrackerJobDetailConfig config : jobDetailConfigList) {
            if(OUTPUT_PROCESSOR_INDICATOR.equalsIgnoreCase(config.getProcessorName())) {
                return config;
            }
        }
        return null;
    }

    public static MonitorTrackerJobDetailConfig getAlertProcessorJobDetailConfig(List<MonitorTrackerJobDetailConfig> jobDetailConfigList) {
        if(CollectionUtils.isEmpty(jobDetailConfigList)) {
            return null;
        }

        for (MonitorTrackerJobDetailConfig config : jobDetailConfigList) {
            if(ALERT_PROCESSOR_INDICATOR.equalsIgnoreCase(config.getProcessorName())) {
                return config;
            }
        }
        return null;
    }
}
