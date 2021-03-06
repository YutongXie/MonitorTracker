package com.huitong.monitortracker.executor;

import com.huitong.monitortracker.dao.ConfigurationDAO;
import com.huitong.monitortracker.dao.mybatis.config.ConfigMapper;
import com.huitong.monitortracker.entity.MonitorTrackerJobConfigs;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import com.huitong.monitortracker.processor.AlertProcessor;
import com.huitong.monitortracker.processor.BusinessProcessor;
import com.huitong.monitortracker.processor.InputProcessor;
import com.huitong.monitortracker.processor.OutputProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class MonitorTrackerExecutor implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(MonitorTrackerExecutor.class);
    @Autowired
    private ConfigurationDAO configurationDAO;
    private InputProcessor inputProcessor;
    private OutputProcessor outputProcessor;
    private BusinessProcessor businessProcessor;
    private AlertProcessor alertProcessor;
    @Autowired
    private ConfigMapper configMapper;
    private void execute() {
        //1. get job config from DB
        List<MonitorTrackerJobConfigs> jobConfigsList = getJobConfig();
        for (MonitorTrackerJobConfigs jobConfig : jobConfigsList) {
            List<MonitorTrackerJobDetailConfig> detailConfigList = getJobDetailConfig(jobConfig.getJobId());
            MonitorTrackerJobExecutor jobExecutor = new MonitorTrackerJobExecutor(jobConfig, detailConfigList);
            jobExecutor.execute();
        }
    }

    private List<MonitorTrackerJobConfigs> getJobConfig() {
        return configMapper.getJobConfig();
//        return configurationDAO.getJobConfig();
    }

    private List<MonitorTrackerJobDetailConfig> getJobDetailConfig(long jobId) {
//        return configurationDAO.getJobDetailConfig(jobId);
        return configMapper.getJobDetailConfig(jobId);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        execute();
    }
}
