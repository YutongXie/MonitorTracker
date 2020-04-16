package com.huitong.monitortracker.processor.customize.business;

import com.huitong.monitortracker.dao.NumberUpperLimitBreachBusinessProcessorDAO;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachMetaData;
import com.huitong.monitortracker.entity.OracleColumnType;
import com.huitong.monitortracker.executor.ExecutorThreadLocal;
import com.huitong.monitortracker.processor.BusinessProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class NumberUpperLimitBreachBusinessProcessor implements BusinessProcessor {

    private Logger logger = LoggerFactory.getLogger(NumberUpperLimitBreachBusinessProcessor.class);
    @Autowired
    private NumberUpperLimitBreachBusinessProcessorDAO businessProcessorDAO;
    @Override
    public void execute() {
        try {
            List<NumberUpperLimitBreachMetaData> inputData = getInputData();
            if(!CollectionUtils.isEmpty(inputData)) {
                List<NumberUpperLimitBreachResult> resultList = processInputData(inputData);
                ExecutorThreadLocal.setBusinessData(resultList);
            }
        } catch (Exception ex) {
           logger.error("NumberUpperLimitBreachBusinessProcessor - failed to process", ex);
        }
    }

    private List<NumberUpperLimitBreachResult> processInputData(List<NumberUpperLimitBreachMetaData> inputData) {
        ForkJoinPool pool = null;
        try {
            Map<String, List<NumberUpperLimitBreachMetaData>> statisticMap = classificationToMap(inputData);
            List<List<NumberUpperLimitBreachMetaData>> fullList = new ArrayList<>(statisticMap.values());
            pool = ForkJoinPool.commonPool();
            List<NumberUpperLimitBreachResult> resultList = pool.invoke(new NumberUpperLimitBreachForkJoinBusinessExecutor(0, fullList.size(), fullList));
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
        Map<String, List<NumberUpperLimitBreachMetaData>> metaDataMap = new HashMap<>();
        for (NumberUpperLimitBreachMetaData metaData : metaDataList) {
            String key = metaData.getSchemaName();
            if(metaDataMap.containsKey(key)) {
                metaDataMap.get(key).add(metaData);
            } else {
                List<NumberUpperLimitBreachMetaData> list = new ArrayList<>();
                list.add(metaData);
                metaDataMap.put(key, list);
            }
        }
        return metaDataMap;
    }
}
