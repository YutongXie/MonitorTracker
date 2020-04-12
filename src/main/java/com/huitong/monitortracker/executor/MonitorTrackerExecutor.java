package com.huitong.monitortracker.executor;

import com.huitong.monitortracker.dao.ConfigurationDAO;
import com.huitong.monitortracker.entity.MonitorTrackerJobConfigs;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import com.huitong.monitortracker.processor.AlertProcessor;
import com.huitong.monitortracker.processor.BusinessProcessor;
import com.huitong.monitortracker.processor.InputProcessor;
import com.huitong.monitortracker.processor.OutputProcessor;
import javafx.scene.control.Alert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class MonitorTrackerExecutor {
    private final Logger logger = LoggerFactory.getLogger(MonitorTrackerExecutor.class);
    @Autowired
    private ConfigurationDAO configurationDAO;
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
            try {
                initialProcess(monitorTrackerJobConfigs);
                inputProcessor.execute();
                businessProcessor.execute();
                outputProcessor.execute();
                if(alertProcessor != null)
                    alertProcessor.execute();
            } catch (Exception ex) {
                logger.error("Execute Monitor Tracker job failed. JobConfig is:{}, exception:{}", monitorTrackerJobConfigs.toString(), ex);
            }
        }
    }

    private List<MonitorTrackerJobConfigs> getJobConfig() {
        return configurationDAO.getJobConfig();
    }

    private List<MonitorTrackerJobDetailConfig> getJobDetailConfig(long jobId) {
        return configurationDAO.getJobDetailConfig(jobId);
    }

    private void initialProcess(MonitorTrackerJobConfigs monitorTrackerJobConfigs) throws Exception {
        String inputProcessorName = monitorTrackerJobConfigs.getInputProcessor();
        String businessProcessorName = monitorTrackerJobConfigs.getBusinessProcessor();
        String outputProcessorName = monitorTrackerJobConfigs.getOutputProcessor();
        String alertProcessorName = monitorTrackerJobConfigs.getAlertProcessor();

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
