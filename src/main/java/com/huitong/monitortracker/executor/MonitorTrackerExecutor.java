package com.huitong.monitortracker.executor;

import com.huitong.monitortracker.entity.MonitorTrackerJobConfigs;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import com.huitong.monitortracker.processor.BusinessProcessor;
import com.huitong.monitortracker.processor.InputProcessor;
import com.huitong.monitortracker.processor.OutputProcessor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MonitorTrackerExecutor {
    private InputProcessor inputProcessor;
    private OutputProcessor outputProcessor;
    private BusinessProcessor businessProcessor;

    private void execute() {
        //1. get job config from DB
        List<MonitorTrackerJobConfigs> monitorTrackerJobConfigsList = getJobConfig();
        for (MonitorTrackerJobConfigs monitorTrackerJobConfigs : monitorTrackerJobConfigsList) {
            List<MonitorTrackerJobDetailConfig> monitorTrackerJobDetailConfigList = getJobDetailConfig(monitorTrackerJobConfigs.getJobId());
            //2. Initial processor base on Job config
            //3. execute
            inputProcessor.execute();
            businessProcessor.execute();
            outputProcessor.execute();

        }
    }

    private List<MonitorTrackerJobConfigs> getJobConfig() {
        return null;
    }

    private List<MonitorTrackerJobDetailConfig> getJobDetailConfig(Long jobId) {
        return null;
    }

    private void initialProcess(MonitorTrackerJobConfigs monitorTrackerJobConfigs) {
        String inputProcessorName = monitorTrackerJobConfigs.getInputProcessor();
        String businessProcessorName = monitorTrackerJobConfigs.getBusinessProcessor();
        String outputProcessorName = monitorTrackerJobConfigs.getOutputProcessor();
        String alertProcessorName = monitorTrackerJobConfigs.getAlertProcessor();

        if(StringUtils.isBlank(inputProcessorName)) {
            
        }
    }
}
