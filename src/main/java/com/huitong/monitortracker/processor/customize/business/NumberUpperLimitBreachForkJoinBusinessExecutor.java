package com.huitong.monitortracker.processor.customize.business;

import com.huitong.monitortracker.dao.NumberUpperLimitBreachBusinessProcessorDAO;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachMetaData;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import com.huitong.monitortracker.entity.OracleColumnType;
import com.huitong.monitortracker.executor.ForkJoinExecutor;
import com.huitong.monitortracker.executor.MonitorTrackerApplicationContextAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class NumberUpperLimitBreachForkJoinBusinessExecutor extends RecursiveTask<List<NumberUpperLimitBreachResult>> {
    private Logger logger = LoggerFactory.getLogger(NumberUpperLimitBreachForkJoinBusinessExecutor.class);
    private int threshold = 2;
    private int startIndex;
    private int endIndex;
    private List<List<NumberUpperLimitBreachMetaData>> fullMetaDataList;
    private NumberUpperLimitBreachBusinessProcessorDAO businessProcessorDAO;

    public NumberUpperLimitBreachForkJoinBusinessExecutor(int startIndex, int endIndex, List<List<NumberUpperLimitBreachMetaData>> fullMetaDataList) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.fullMetaDataList = fullMetaDataList;
    }

    @Override
    protected List<NumberUpperLimitBreachResult> compute() {
        List<NumberUpperLimitBreachResult> resultList = new ArrayList<>();
        if((endIndex - startIndex) < threshold) {
            initialResource();
            for (int i = startIndex; i < endIndex; i++) {
                List<NumberUpperLimitBreachMetaData> metaDataList = fullMetaDataList.get(i);
                Map<String, List<NumberUpperLimitBreachMetaData>> statisticMap = classificationToMap(metaDataList);
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
            }
        } else {
            int middleIndex = (endIndex + startIndex) / threshold;
            NumberUpperLimitBreachForkJoinBusinessExecutor lefForkJoin = new NumberUpperLimitBreachForkJoinBusinessExecutor(startIndex, middleIndex, fullMetaDataList);
            NumberUpperLimitBreachForkJoinBusinessExecutor rightForkJoin = new NumberUpperLimitBreachForkJoinBusinessExecutor(middleIndex, endIndex, fullMetaDataList);

            invokeAll(lefForkJoin, rightForkJoin);
            resultList.addAll(lefForkJoin.join());
            resultList.addAll(rightForkJoin.join());
        }

        return resultList;
    }

    private void initialResource() {
        if(businessProcessorDAO == null) {
            businessProcessorDAO = (NumberUpperLimitBreachBusinessProcessorDAO) MonitorTrackerApplicationContextAware.getBean("numberUpperLimitBreachBusinessProcessorDAOImpl");
        }
    }

    public Map<String, List<NumberUpperLimitBreachMetaData>> classificationToMap(List<NumberUpperLimitBreachMetaData> metaDataList) {
        Map<String, List<NumberUpperLimitBreachMetaData>> metaDataMap = new HashMap<>();
        for (NumberUpperLimitBreachMetaData metaData : metaDataList) {
            String key = metaData.getSchemaName() + "_" + metaData.getTableName();
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

    private BigDecimal convertToDecimal(String columnCurrentValueStr) {
        BigDecimal result = BigDecimal.ZERO;
        try {
            result = new BigDecimal(columnCurrentValueStr);
        } catch (Exception ex) {
            logger.warn("Convert String to Decimal failed. number string is:{}", columnCurrentValueStr);
        }
        return result;
    }

    private String getLimitValue(List<NumberUpperLimitBreachMetaData> metaDataList, String columnName) {
        for (NumberUpperLimitBreachMetaData metaData : metaDataList) {
            if (columnName.equalsIgnoreCase(metaData.getColumnName())) {
                String dataType = metaData.getDataType();
                if(OracleColumnType.BINARY_DOUBLE.name().equalsIgnoreCase(dataType)) {
                    return OracleColumnType.BINARY_DOUBLE.getMaxValue();
                } else if(OracleColumnType.BINARY_FLOAT.name().equalsIgnoreCase(dataType)) {
                    return OracleColumnType.BINARY_FLOAT.getMaxValue();
                } else if(OracleColumnType.FLOAT.name().equalsIgnoreCase(dataType)) {
                    return OracleColumnType.FLOAT.getMaxValue();
                } else if(OracleColumnType.NUMBER.name().equalsIgnoreCase(dataType)) {
                    Long dataScale = Optional.ofNullable(metaData.getDataScale()).orElse(0L);
                    Long dataPrecision = Optional.ofNullable(metaData.getDataPrecision()).orElse(0L);
                    return generateLimitValueForNumberType(dataPrecision, dataScale);
                }
            }
        }
        return "0";
    }

    private String generateLimitValueForNumberType(Long dataPrecision, Long dataScale) {
        String result = OracleColumnType.NUMBER.getMaxValue();
        if(dataPrecision == 0) {
            return result;
        }
        if(dataPrecision <= dataScale)
            return result;
        StringBuffer sb = new StringBuffer();
        long length = dataPrecision - dataScale;
        for (long i = 0; i < length; i++) {
            sb.append("9");
        }
        if(sb.length() > 5) {
            result = OracleColumnType.NUMBER.getDisplayFormat() + (sb.length() -1);
        } else {
            result = sb.toString();
        }
        return result;
    }

    private String generateCurrentValueSQL(List<NumberUpperLimitBreachMetaData> metaDataList) {
        StringBuffer sb = new StringBuffer();
        for (NumberUpperLimitBreachMetaData metaData : metaDataList) {
            if(sb.length() > 0) {
                sb.append("||';'||");
            }
            sb.append("'").append(metaData.getColumnName()).append("-'||").append("MAX(").append(metaData.getColumnName()).append(") ");
        }
        return "SELECT " + sb.toString() + " FROM " + metaDataList.get(0).getSchemaName() + "." + metaDataList.get(0).getTableName();
    }
}
