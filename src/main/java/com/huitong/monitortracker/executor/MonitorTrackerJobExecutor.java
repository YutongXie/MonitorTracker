package com.huitong.monitortracker.executor;

import com.huitong.monitortracker.entity.MonitorTrackerJobConfigs;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import com.huitong.monitortracker.processor.AlertProcessor;
import com.huitong.monitortracker.processor.BusinessProcessor;
import com.huitong.monitortracker.processor.InputProcessor;
import com.huitong.monitortracker.processor.OutputProcessor;
import com.huitong.monitortracker.utils.MonitorTrackerConfigurationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Slf4j
public class MonitorTrackerJobExecutor {
    private MonitorTrackerJobConfigs jobConfig;
    private List<MonitorTrackerJobDetailConfig> detailConfig;

    private InputProcessor inputProcessor;
    private OutputProcessor outputProcessor;
    private BusinessProcessor businessProcessor;
    private AlertProcessor alertProcessor;

    public MonitorTrackerJobExecutor(MonitorTrackerJobConfigs jobConfig, List<MonitorTrackerJobDetailConfig> detailConfig) {
        this.jobConfig = jobConfig;
        this.detailConfig = detailConfig;
    }

    public void execute() {
        try {
            initialProcess(jobConfig);
            inputProcessor.execute(MonitorTrackerConfigurationUtils.getInputProcessorJobDetailConfig(detailConfig));
            businessProcessor.execute(MonitorTrackerConfigurationUtils.getBusinessProcessorJobDetailConfig(detailConfig));
            outputProcessor.execute(MonitorTrackerConfigurationUtils.getOutputProcessorJobDetailConfig(detailConfig));
            if(alertProcessor != null)
                alertProcessor.execute(MonitorTrackerConfigurationUtils.getAlertProcessorJobDetailConfig(detailConfig));
        } catch (Exception ex) {
            log.error("Execute Monitor Tracker job failed. JobConfig is:{}, exception:{}", jobConfig.toString(), ex);
        }
    }
    private void initialProcess(MonitorTrackerJobConfigs jobConfig) throws Exception {
        String inputProcessorName = jobConfig.getInputProcessor();
        String businessProcessorName = jobConfig.getBusinessProcessor();
        String outputProcessorName = jobConfig.getOutputProcessor();
        String alertProcessorName = jobConfig.getAlertProcessor();

        if(StringUtils.isNotBlank(inputProcessorName)) {
            inputProcessor = (InputProcessor) MonitorTrackerApplicationContextAware.getBean(inputProcessorName);
            if(inputProcessor == null)
                throw new Exception("Invalid InputProcessor setup");
        } else {
            throw new Exception("Missing InputProcessor setup");
        }

        if(StringUtils.isNotBlank(businessProcessorName)) {
            businessProcessor = (BusinessProcessor) MonitorTrackerApplicationContextAware.getBean(businessProcessorName);
            if(businessProcessor == null)
                throw new Exception("Invalid BusinessProcessor setup");
        } else {
            throw new Exception("Missing BusinessProcessor setup");
        }

        if(StringUtils.isNotBlank(outputProcessorName)) {
            outputProcessor = (OutputProcessor) MonitorTrackerApplicationContextAware.getBean(outputProcessorName);
            if(outputProcessor == null)
                throw new Exception("Invalid OutputProcessor setup");
        } else {
            throw new Exception("Missing OutputProcessor setup");
        }

        if(StringUtils.isNotBlank(alertProcessorName)) {
            alertProcessor = (AlertProcessor) MonitorTrackerApplicationContextAware.getBean(alertProcessorName);
            if(alertProcessor == null)
                throw new Exception("Invalid AlertProcessor setup");
        }
    }
}
