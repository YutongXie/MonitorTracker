package com.huitong.monitortracker.processor.customize.output;

import com.huitong.monitortracker.dao.NumberUpperLimitBreachOutputProcessorDAO;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachMetaData;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import com.huitong.monitortracker.executor.ExecutorThreadLocal;
import com.huitong.monitortracker.processor.OutputProcessor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
@Component
@Slf4j
public class NumberUpperLimitBreachOutputProcessor implements OutputProcessor {

    @Override
    public void execute(MonitorTrackerJobDetailConfig config) {
        try {
            List<NumberUpperLimitBreachResult> inputData = getInputData();
            if(!CollectionUtils.isEmpty(inputData)) {
                processInputData(inputData, config);
            }
            ExecutorThreadLocal.setOutputData(inputData);
        } catch (Exception ex) {
            log.error("NumberUpperLimitBreachOutputProcessor - failed to process.", ex);
        }
    }

    private void processInputData(List<NumberUpperLimitBreachResult> inputData, MonitorTrackerJobDetailConfig config) {
        ForkJoinPool pool = null;
        try {
            Map<String, List<NumberUpperLimitBreachResult>> lastResultMap = classificationToMap(inputData);
            List<List<NumberUpperLimitBreachResult>> fullList = new ArrayList<>(lastResultMap.values());
            pool = ForkJoinPool.commonPool();
            pool.invoke(new NumberUpperLimitBreachForkJoinOutputExecutor(0, fullList.size(), fullList, config));
        } finally {
            if(pool != null) {
                pool.shutdown();
            }
        }
    }

    private List<NumberUpperLimitBreachResult> getInputData() {
        List<NumberUpperLimitBreachResult> inputData = new ArrayList<>();
        Object obj = ExecutorThreadLocal.getBusinessData();
        if(obj instanceof List) {
            if (((List) obj).size() > 0 && ((List) obj).get(0) instanceof NumberUpperLimitBreachResult) {
                inputData = (List<NumberUpperLimitBreachResult>) obj;
            }
        }
        return inputData;
    }

    public Map<String, List<NumberUpperLimitBreachResult>> classificationToMap(List<NumberUpperLimitBreachResult> resultList) {
        Map<String, List<NumberUpperLimitBreachResult>> metaDataMap = new HashMap<>();
        for (NumberUpperLimitBreachResult result : resultList) {
            String key = result.getSchemaName();
            if(metaDataMap.containsKey(key)) {
                metaDataMap.get(key).add(result);
            } else {
                List<NumberUpperLimitBreachResult> list = new ArrayList<>();
                list.add(result);
                metaDataMap.put(key, list);
            }
        }
        return metaDataMap;
    }

}
