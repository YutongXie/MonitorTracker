package com.huitong.monitortracker.processor.customize.business;

import com.huitong.monitortracker.dao.NumberUpperLimitBreachBusinessProcessorDAO;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachStatistic;
import com.huitong.monitortracker.executor.ExecutorThreadLocal;
import com.huitong.monitortracker.processor.BusinessProcessor;
import jdk.internal.org.objectweb.asm.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

public class NumberUpperLimitBreachBusinessProcessor implements BusinessProcessor {

    private Logger logger = LoggerFactory.getLogger(NumberUpperLimitBreachBusinessProcessor.class);
    @Autowired
    private NumberUpperLimitBreachBusinessProcessorDAO businessProcessorDAO;
    @Override
    public void execute() {
        Object obj = ExecutorThreadLocal.getInputData();
        if(obj instanceof List) {
            if(((List) obj).size() > 0 && ((List) obj).get(0) instanceof NumberUpperLimitBreachStatistic) {
                Map<String, List<NumberUpperLimitBreachStatistic>> statisticMap = classificationToMap((List<NumberUpperLimitBreachStatistic>) obj);
                List<NumberUpperLimitBreachResult> resultList = new ArrayList<>();
                statisticMap.forEach((key, value) ->{
                   String sql = generateCurrentValueSQL(value);
                   String currentValue = businessProcessorDAO.getTableColumnCurrentValue(sql);
                   String[] columnCurrentValueArray = currentValue.split(";");
                    for (String columnCurrentValue : columnCurrentValueArray) {
                        NumberUpperLimitBreachResult result = new NumberUpperLimitBreachResult();
                        result.setSchemaName(value.get(0).getSchemaName());
                        result.setTableName(value.get(0).getTableName());
                        String columnName = columnCurrentValue.substring(0, columnCurrentValue.indexOf("-"));
                        String columnValue = columnCurrentValue.substring(columnCurrentValue.indexOf("-") + 1);
                        result.setColumnName(columnName);
                        result.setCurrentValue(convertToDecimal(columnValue));
                        result.setLimitValue(getLimitValue(value, columnName));
                        result.setActive("A");
                        resultList.add(result);
                    }
                });
                ExecutorThreadLocal.setBusinessData(resultList);
            }
        }
    }

    private BigDecimal convertToDecimal(String columnCurrentValueStr) {
        BigDecimal result = BigDecimal.ZERO;
        try {
            result = new BigDecimal(columnCurrentValueStr);
        } catch (Exception ex) {
            logger.warn("Convert String to Decimal failed. number string is:{}", columnCurrentValueStr);
        }
        return result;
    }

    private BigDecimal getLimitValue(List<NumberUpperLimitBreachStatistic> statisticList, String columnName) {
        for (NumberUpperLimitBreachStatistic statistic : statisticList) {
            if (columnName.equalsIgnoreCase(statistic.getColumnName())) {
                Long dataLength = Optional.ofNullable(statistic.getDataLength()).orElse(0L);
                Long dataPrecision = Optional.ofNullable(statistic.getDataPrecision()).orElse(0L);
                return generateLimitValue(dataLength, dataPrecision);
            }
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal generateLimitValue(Long dataLength, Long dataPrecision) {
        BigDecimal result = BigDecimal.ZERO;
        if(dataLength <= dataPrecision)
            return result;
        StringBuffer sb = new StringBuffer();
        long length = dataLength - dataPrecision;
        for (long i = 0; i < length; i++) {
            sb.append("9");
        }
        if(sb.length() > 0) {
            result = new BigDecimal(sb.toString());
        }
        return result;
    }

    private String generateCurrentValueSQL(List<NumberUpperLimitBreachStatistic> statisticList) {
        StringBuffer sb = new StringBuffer();
        for (NumberUpperLimitBreachStatistic statistic : statisticList) {
            if(sb.length() > 0) {
                sb.append("||';'||");
            }
            sb.append("'").append(statistic.getColumnName()).append("-'||").append("MAX(").append(statistic.getColumnName()).append(") ");
        }
        return "SELECT " + sb.toString() + " FROM " + statisticList.get(0).getSchemaName() + "." + statisticList.get(0).getTableName();
    }

    public Map<String, List<NumberUpperLimitBreachStatistic>> classificationToMap(List<NumberUpperLimitBreachStatistic> statisticList) {
        Map<String, List<NumberUpperLimitBreachStatistic>> statisticMap = new HashMap<>();
        for (NumberUpperLimitBreachStatistic statistic : statisticList) {
            String key = statistic.getSchemaName() + "_" + statistic.getTableName();
            if(statisticMap.containsKey(key)) {
                statisticMap.get(key).add(statistic);
            } else {
                List<NumberUpperLimitBreachStatistic> list = new ArrayList<>();
                list.add(statistic);
                statisticMap.put(key, list);
            }
        }
        return statisticMap;
    }
}
