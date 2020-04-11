package com.huitong.monitortracker.executor;

import com.huitong.monitortracker.entity.MonitorTrackerJobConfigs;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import com.huitong.monitortracker.processor.AlertProcessor;
import com.huitong.monitortracker.processor.BusinessProcessor;
import com.huitong.monitortracker.processor.InputProcessor;
import com.huitong.monitortracker.processor.OutputProcessor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MonitorTrackerExecutor {
    private InputProcessor inputProcessor;
    private OutputProcessor outputProcessor;
    private BusinessProcessor businessProcessor;
    private AlertProcessor alertProcessor;

    private void execute() {
        //1. get job config from DB
        List<MonitorTrackerJobConfigs> monitorTrackerJobConfigsList = getJobConfig();
        for (MonitorTrackerJobConfigs monitorTrackerJobConfigs : monitorTrackerJobConfigsList) {
            List<MonitorTrackerJobDetailConfig> monitorTrackerJobDetailConfigList = getJobDetailConfig(monitorTrackerJobConfigs.getJobId());
            //2. Initial processor base on Job config
            if(initialProcess(monitorTrackerJobConfigs)) {
                //3. execute
                inputProcessor.execute();
                businessProcessor.execute();
                outputProcessor.execute();
                if(alertProcessor != null) {
                    alertProcessor.execute();
                }
            }

        }
    }

    private List<MonitorTrackerJobConfigs> getJobConfig() {
        return null;
    }

    private List<MonitorTrackerJobDetailConfig> getJobDetailConfig(Long jobId) {
        return null;
    }

    private boolean initialProcess(MonitorTrackerJobConfigs monitorTrackerJobConfigs) {
        String inputProcessorName = monitorTrackerJobConfigs.getInputProcessor();
        String businessProcessorName = monitorTrackerJobConfigs.getBusinessProcessor();
        String outputProcessorName = monitorTrackerJobConfigs.getOutputProcessor();
        String alertProcessorName = monitorTrackerJobConfigs.getAlertProcessor();

        if(StringUtils.isBlank(inputProcessorName)) {
            return false;
        }

        if(StringUtils.isBlank(businessProcessorName)) {
            return false;
        }

        if(StringUtils.isBlank(outputProcessorName)) {
            return false;
        }

        if(StringUtils.isNotBlank(alertProcessorName)) {

        }
        return true;
    }
}
