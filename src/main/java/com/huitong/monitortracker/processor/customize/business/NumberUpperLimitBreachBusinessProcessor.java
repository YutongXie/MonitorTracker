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
            if(((List) obj).size() > 0 && ((List) obj).get(0) instanceof NumberUpperLimitBreachMetaData) {
                Map<String, List<NumberUpperLimitBreachMetaData>> statisticMap = classificationToMap((List<NumberUpperLimitBreachMetaData>) obj);
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
}
