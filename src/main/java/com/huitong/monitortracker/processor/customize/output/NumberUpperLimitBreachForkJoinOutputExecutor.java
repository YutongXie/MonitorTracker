package com.huitong.monitortracker.processor.customize.output;

import com.huitong.monitortracker.dao.NumberUpperLimitBreachBusinessProcessorDAO;
import com.huitong.monitortracker.dao.NumberUpperLimitBreachOutputProcessorDAO;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.RecursiveAction;

public class NumberUpperLimitBreachForkJoinOutputExecutor extends RecursiveAction {
    private Logger logger = LoggerFactory.getLogger(NumberUpperLimitBreachForkJoinOutputExecutor.class);
    private final String DAYS_REACH_TO_80_PERCENT_MORE_THAN_90 = "> 90";
    private final String DAYS_REACH_TO_80_PERCENT_MORE_THAN_60 = "> 60";
    private final String DAYS_REACH_TO_80_PERCENT_MORE_THAN_30 = "> 30";
    private final String DAYS_REACH_TO_80_PERCENT_MORE_THAN_15 = "> 15";
    private final String DAYS_REACH_TO_80_PERCENT_LESS_THAN_15 = "< 15";

    private int threshold = 2;
    private int startIndex;
    private int endIndex;
    private List<List<NumberUpperLimitBreachResult>> fullResultList;
    private NumberUpperLimitBreachOutputProcessorDAO outputProcessorDAO;

    @Value("sql.outputprocessor.insert_new_result")
    private String sql_insert_new_result;

    public NumberUpperLimitBreachForkJoinOutputExecutor(int startIndex, int endIndex, List<List<NumberUpperLimitBreachResult>> fullResultList) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.fullResultList = fullResultList;
    }

    @Override
    protected void compute() {
        if((endIndex - startIndex) < threshold) {
            for (int i = startIndex; i < endIndex; i++) {
                List<NumberUpperLimitBreachResult> currentResultList = fullResultList.get(i);
                List<NumberUpperLimitBreachResult> lastResult = outputProcessorDAO.getLastResult();
                outputProcessorDAO.inactiveLastResult();

                Map<String, NumberUpperLimitBreachResult> lastResultMap = classificationToMap(lastResult);
                for (NumberUpperLimitBreachResult result : currentResultList) {
                    BigDecimal burnRate = generateBurnRate(lastResultMap.get(result.getSchemaName() + "_" + result.getTableName() + "_" + result.getColumnName()), result);
                    result.setBurnRate(burnRate);
                    result.setUsePercent(calculateUsePercent(result.getCurrentValue(), result.getLimitValue()));
                    result.setDaysReach80Percent(calculateDaysReach80Percent(result.getBurnRate(), result.getLimitValue()));
                }
                outputProcessorDAO.save((String[]) generateInsertSQL(currentResultList).toArray());
            }

        } else {
            int middleIndex = (endIndex + startIndex) / threshold;
            NumberUpperLimitBreachForkJoinOutputExecutor leftExecutor = new NumberUpperLimitBreachForkJoinOutputExecutor(startIndex, middleIndex, fullResultList);
            NumberUpperLimitBreachForkJoinOutputExecutor rightExecutor = new NumberUpperLimitBreachForkJoinOutputExecutor(middleIndex, endIndex, fullResultList);
            invokeAll(leftExecutor, rightExecutor);
        }
    }

    public Map<String, NumberUpperLimitBreachResult> classificationToMap(List<NumberUpperLimitBreachResult> resultList) {
        Map<String, NumberUpperLimitBreachResult> resultMap = new HashMap<>();
        for (NumberUpperLimitBreachResult result : resultList) {
            String key = result.getSchemaName() + "_" + result.getTableName() + "_" + result.getColumnName();
            if(!resultMap.containsKey(key)) {
                resultMap.put(key, result);
            }
        }
        return resultMap;
    }

    private List<String> generateInsertSQL(List<NumberUpperLimitBreachResult> currentResultList) {
        List<String> sqlList = new ArrayList<>();
        for (NumberUpperLimitBreachResult result : currentResultList) {
            String sql = sql_insert_new_result.replace(":schemaName", "'" + result.getSchemaName() + "'")
                    .replace(":tableName", "'" + result.getTableName() + "'")
                    .replace(":columnName", "'" + result.getColumnName() + "'")
                    .replace(":currentValue", result.getCurrentValue() + "")
                    .replace(":limitValue", "'" + result.getLimitValue() + "'")
                    .replace(":burnRate", result.getBurnRate() + "")
                    .replace(":daysReach80%", "'" + result.getDaysReach80Percent() + "'")
                    .replace(":active", "'" + result.getActive() + "'");
            sqlList.add(sql);
        }
        return sqlList;
    }

    private BigDecimal generateBurnRate(NumberUpperLimitBreachResult lastResult, NumberUpperLimitBreachResult currentResult) {
        BigDecimal lastValue = Optional.ofNullable(lastResult.getCurrentValue()).orElse(BigDecimal.ZERO);
        BigDecimal currentValue = Optional.ofNullable(currentResult.getCurrentValue()).orElse(BigDecimal.ZERO);
        return currentValue.subtract(lastValue);
    }

    private String calculateUsePercent(BigDecimal currentValue, String limitValue) {
        if(BigDecimal.ZERO.compareTo(currentValue) == 0)
            return "0.00%";
        if("0".equalsIgnoreCase(limitValue)) {
            return "0.00%";
        }
        if(limitValue.contains("9.99...")) {
            int index = limitValue.indexOf("E");
            int count = Integer.valueOf(limitValue.substring(index + 1));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < count; i++) {
                sb.append(9);
            }
            limitValue = limitValue.replace("...", sb.toString());
        }

        BigDecimal limitValueNum = new BigDecimal(limitValue);
        return currentValue.multiply(new BigDecimal(100)).divide(limitValueNum, 2, BigDecimal.ROUND_DOWN) + "%";
    }

    private String calculateDaysReach80Percent(BigDecimal burnRate, String limitValue) {
        if(BigDecimal.ZERO.compareTo(burnRate) == 0) {
            return DAYS_REACH_TO_80_PERCENT_MORE_THAN_90;
        }

        if("0".equalsIgnoreCase(limitValue)) {
            return DAYS_REACH_TO_80_PERCENT_MORE_THAN_90;
        }
        if(limitValue.contains("9.99...")) {
            int index = limitValue.indexOf("E");
            int count = Integer.valueOf(limitValue.substring(index + 1));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < count; i++) {
                sb.append(9);
            }
            limitValue = limitValue.replace("...", sb.toString());
        }

        BigDecimal limitValueNum = new BigDecimal(limitValue);

        long days = limitValueNum.divide(burnRate, 0, BigDecimal.ROUND_HALF_UP).longValue();
        if(days > 90) {
            return DAYS_REACH_TO_80_PERCENT_MORE_THAN_90;
        } else if(days > 60) {
            return DAYS_REACH_TO_80_PERCENT_MORE_THAN_60;
        } else if(days > 30) {
            return DAYS_REACH_TO_80_PERCENT_MORE_THAN_30;
        } else if(days >= 15) {
            return DAYS_REACH_TO_80_PERCENT_MORE_THAN_15;
        } else {
            return DAYS_REACH_TO_80_PERCENT_LESS_THAN_15;
        }
    }
}
