package com.huitong.monitortracker.processor.customize.input;

import com.huitong.monitortracker.dao.NumberUpperLimitBreachInputProcessorDAO;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachStatistic;
import com.huitong.monitortracker.executor.ExecutorThreadLocal;
import com.huitong.monitortracker.processor.InputProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class NumberUpperLimitBreachInputProcessor implements InputProcessor {
    @Autowired
    private NumberUpperLimitBreachInputProcessorDAO inputProcessorDAO;

    @Override
    public void execute() {
        List<NumberUpperLimitBreachStatistic> statisticList = inputProcessorDAO.getStatistic();
        ExecutorThreadLocal.setInputData(statisticList);
    }
}
