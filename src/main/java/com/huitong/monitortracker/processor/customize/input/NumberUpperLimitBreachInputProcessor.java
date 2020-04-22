package com.huitong.monitortracker.processor.customize.input;

import com.huitong.monitortracker.dao.NumberUpperLimitBreachInputProcessorDAO;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachMetaData;
import com.huitong.monitortracker.executor.ExecutorThreadLocal;
import com.huitong.monitortracker.processor.InputProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@Slf4j
public class NumberUpperLimitBreachInputProcessor implements InputProcessor {
    @Autowired
    private NumberUpperLimitBreachInputProcessorDAO inputProcessorDAO;

    @Override
    public void execute(MonitorTrackerJobDetailConfig config) {
        applyConfig(config);
        List<NumberUpperLimitBreachMetaData> statisticList = inputProcessorDAO.getMetaData();
        ExecutorThreadLocal.setInputData(statisticList);
    }


    private void applyConfig(MonitorTrackerJobDetailConfig config) {

    }
}
