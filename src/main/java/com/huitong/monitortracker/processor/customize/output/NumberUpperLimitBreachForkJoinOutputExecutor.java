package com.huitong.monitortracker.processor.customize.output;

import com.huitong.monitortracker.dao.NumberUpperLimitBreachBusinessProcessorDAO;
import com.huitong.monitortracker.dao.NumberUpperLimitBreachOutputProcessorDAO;
import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachConstants;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NumberUpperLimitBreachForkJoinOutputExecutor extends RecursiveAction {
    private Logger logger = LoggerFactory.getLogger(NumberUpperLimitBreachForkJoinOutputExecutor.class);

    private int startIndex;
    private int endIndex;
    private List<List<NumberUpperLimitBreachResult>> fullResultList;
    private NumberUpperLimitBreachOutputProcessorDAO outputProcessorDAO;
    private MonitorTrackerJobDetailConfig config;

    @Value("sql.outputprocessor.insert_new_result")
    private String sql_insert_new_result;

    public NumberUpperLimitBreachForkJoinOutputExecutor(int startIndex, int endIndex, List<List<NumberUpperLimitBreachResult>> fullResultList, MonitorTrackerJobDetailConfig config) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.fullResultList = fullResultList;
        this.config = config;
    }

    @Override
    protected void compute() {
        if((endIndex - startIndex) < NumberUpperLimitBreachConstants.FORK_JOIN_EXECUTOR_THRESHOLD.getValueAsNumber()) {
            for (int i = startIndex; i < endIndex; i++) {
                List<NumberUpperLimitBreachResult> currentResultList = fullResultList.get(i);
                List<NumberUpperLimitBreachResult> lastResult = outputProcessorDAO.getLastResult();
                outputProcessorDAO.inactiveLastResult(currentResultList.get(0).getSchemaName());

                Map<String, NumberUpperLimitBreachResult> lastResultMap = classificationToMap(lastResult);
                for (NumberUpperLimitBreachResult result : currentResultList) {
                    BigDecimal burnRate = generateBurnRate(lastResultMap.get(result.getSchemaName() + "_" + result.getTableName() + "_" + result.getColumnName()), result);
                    result.setBurnRate(burnRate);
                    result.setUsePercent(calculateUsePercent(result.getCurrentValue(), result.getLimitValue()));
                    result.setDaysReach80Percent(calculateDaysReach80Percent(result.getBurnRate(), result.getLimitValue()));
                }
                outputProcessorDAO.save(currentResultList);
            }

        } else {
            int middleIndex = (endIndex + startIndex) / NumberUpperLimitBreachConstants.FORK_JOIN_EXECUTOR_THRESHOLD.getValueAsNumber();
            NumberUpperLimitBreachForkJoinOutputExecutor leftExecutor = new NumberUpperLimitBreachForkJoinOutputExecutor(startIndex, middleIndex, fullResultList, config);
            NumberUpperLimitBreachForkJoinOutputExecutor rightExecutor = new NumberUpperLimitBreachForkJoinOutputExecutor(middleIndex, endIndex, fullResultList, config);
            invokeAll(leftExecutor, rightExecutor);
        }
    }

    public Map<String, NumberUpperLimitBreachResult> classificationToMap(List<NumberUpperLimitBreachResult> resultList) {
        return resultList.stream()
                .collect(Collectors.toMap(result -> result.getSchemaName() + "_" + result.getTableName() + "_" + result.getColumnName(), Function.identity()));
    }

    private BigDecimal generateBurnRate(NumberUpperLimitBreachResult lastResult, NumberUpperLimitBreachResult currentResult) {
        BigDecimal lastValue = Optional.ofNullable(lastResult.getCurrentValue()).orElse(BigDecimal.ZERO);
        BigDecimal currentValue = Optional.ofNullable(currentResult.getCurrentValue()).orElse(BigDecimal.ZERO);
        BigDecimal burnRate = currentValue.subtract(lastValue);
        if(burnRate.compareTo(BigDecimal.ZERO) > 0) {
            return currentValue.subtract(lastValue);
        } else {
            return BigDecimal.ZERO;
        }
    }

    private String calculateUsePercent(BigDecimal currentValue, String limitValue) {
        if(BigDecimal.ZERO.compareTo(currentValue) == 0)
            return NumberUpperLimitBreachConstants.USE_PERCENT_DEFAULT_VALUE.getValue();
        if("0".equalsIgnoreCase(limitValue)) {
            return NumberUpperLimitBreachConstants.USE_PERCENT_DEFAULT_VALUE.getValue();
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
            return NumberUpperLimitBreachConstants.DAYS_REACH_TO_80_PERCENT_MORE_THAN_90.getValue();
        }

        if("0".equalsIgnoreCase(limitValue)) {
            return NumberUpperLimitBreachConstants.DAYS_REACH_TO_80_PERCENT_MORE_THAN_90.getValue();
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
        BigDecimal divided = limitValueNum.divide(burnRate, 0, BigDecimal.ROUND_HALF_UP);
        long days = divided.compareTo(new BigDecimal(90)) > 0 ? 91 : divided.longValue();
        if(days > 90) {
            return NumberUpperLimitBreachConstants.DAYS_REACH_TO_80_PERCENT_MORE_THAN_90.getValue();
        } else if(days > 60) {
            return NumberUpperLimitBreachConstants.DAYS_REACH_TO_80_PERCENT_MORE_THAN_60.getValue();
        } else if(days > 30) {
            return NumberUpperLimitBreachConstants.DAYS_REACH_TO_80_PERCENT_MORE_THAN_30.getValue();
        } else if(days >= 15) {
            return NumberUpperLimitBreachConstants.DAYS_REACH_TO_80_PERCENT_MORE_THAN_15.getValue();
        } else {
            return NumberUpperLimitBreachConstants.DAYS_REACH_TO_80_PERCENT_LESS_THAN_15.getValue();
        }
    }
}
