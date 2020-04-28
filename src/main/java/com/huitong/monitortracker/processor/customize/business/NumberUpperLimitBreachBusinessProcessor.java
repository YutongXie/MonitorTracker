package com.huitong.monitortracker.processor.customize.business;

import com.huitong.monitortracker.dao.NumberUpperLimitBreachBusinessProcessorDAO;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachMetaData;
import com.huitong.monitortracker.entity.OracleColumnType;
import com.huitong.monitortracker.executor.ExecutorThreadLocal;
import com.huitong.monitortracker.processor.BusinessProcessor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Component
@Slf4j
public class NumberUpperLimitBreachBusinessProcessor implements BusinessProcessor {

    @Autowired
    private NumberUpperLimitBreachBusinessProcessorDAO businessProcessorDAO;
    @Override
    public void execute(MonitorTrackerJobDetailConfig config) {
        try {
            List<NumberUpperLimitBreachMetaData> inputData = getInputData();
            if(!CollectionUtils.isEmpty(inputData)) {
                List<NumberUpperLimitBreachResult> resultList = processInputData(inputData, config);
                ExecutorThreadLocal.setBusinessData(resultList);
            }
        } catch (Exception ex) {
           log.error("NumberUpperLimitBreachBusinessProcessor - failed to process", ex);
        }
    }

    private List<NumberUpperLimitBreachResult> processInputData(List<NumberUpperLimitBreachMetaData> inputData, MonitorTrackerJobDetailConfig config) {
        ForkJoinPool pool = null;
        try {
            Map<String, List<NumberUpperLimitBreachMetaData>> metaDataMap = classificationToMap(inputData);
            List<List<NumberUpperLimitBreachMetaData>> fullList = new ArrayList<>(metaDataMap.values());
            pool = ForkJoinPool.commonPool();
            List<NumberUpperLimitBreachResult> resultList = pool.invoke(new NumberUpperLimitBreachForkJoinBusinessExecutor(0, fullList.size(), fullList, config));
            return resultList;
        } finally {
            if(pool != null) {
                pool.shutdown();
            }
        }
    }

    private List<NumberUpperLimitBreachMetaData> getInputData() {
        List<NumberUpperLimitBreachMetaData> inputData = new ArrayList<>();
        Object obj = ExecutorThreadLocal.getInputData();
        if(obj instanceof List) {
            if(((List) obj).size() > 0 && ((List) obj).get(0) instanceof NumberUpperLimitBreachMetaData) {
                inputData = (List<NumberUpperLimitBreachMetaData>) obj;
            }
        }
        return inputData;
    }

    public Map<String, List<NumberUpperLimitBreachMetaData>> classificationToMap(List<NumberUpperLimitBreachMetaData> metaDataList) {
        return metaDataList.stream().collect(Collectors.groupingBy(NumberUpperLimitBreachMetaData::getSchemaName));
    }
}
