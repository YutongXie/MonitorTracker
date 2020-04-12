package com.huitong.monitortracker.processor.customize.output;

import com.huitong.monitortracker.dao.NumberUpperLimitBreachOutputProcessorDAO;
import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;
import com.huitong.monitortracker.executor.ExecutorThreadLocal;
import com.huitong.monitortracker.processor.OutputProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.*;

public class NumberUpperLimitBreachOutputProcessor implements OutputProcessor {
    @Autowired
    private NumberUpperLimitBreachOutputProcessorDAO outputProcessorDAO;
    @Value("sql.outputprocessor.insert_new_result")
    private String sql_insert_new_result;
    @Override
    public void execute() {
        Object obj = ExecutorThreadLocal.getBusinessData();
        if(obj instanceof List) {
            if(((List) obj).size() > 0 && ((List) obj).get(0) instanceof NumberUpperLimitBreachResult) {
                List<NumberUpperLimitBreachResult> lastResult = outputProcessorDAO.getLastResult();
                outputProcessorDAO.inactiveLastResult();
                List<NumberUpperLimitBreachResult> currentResultList = (List<NumberUpperLimitBreachResult>) obj;
                Map<String, NumberUpperLimitBreachResult> lastResultMap = classificationToMap(lastResult);
                for (NumberUpperLimitBreachResult result : currentResultList) {
                    BigDecimal burnRate = generateBurnRate(lastResultMap.get(result.getSchemaName() + "_" + result.getTableName() + "_" + result.getColumnName()), result);
                    result.setBurnRate(burnRate);
                    result.setUsePercent(calculateUsePencent(result.getCurrentValue(), result.getLimitValue()));
                    result.setDaysReach80Percent(calculateDaysReach80Percent(result.getBurnRate(), result.getLimitValue()));
                    result.setDaysReachFull(calculateDaysReachFull(result.getBurnRate(), result.getLimitValue()));
                }
                outputProcessorDAO.save((String[]) generateInsertSQL(currentResultList).toArray());
            }
        }
    }

    private List<String> generateInsertSQL(List<NumberUpperLimitBreachResult> currentResultList) {
        List<String> sqlList = new ArrayList<>();
        for (NumberUpperLimitBreachResult result : currentResultList) {
            String sql = sql_insert_new_result.replace(":schemaName", "'" + result.getSchemaName() + "'")
                    .replace(":tableName", "'" + result.getTableName() + "'")
                    .replace(":columnName", "'" + result.getColumnName() + "'")
                    .replace(":currentValue", result.getCurrentValue() + "")
                    .replace(":limitValue", result.getLimitValue() + "")
                    .replace(":burnRate", result.getBurnRate() + "")
                    .replace(":daysReach80%", result.getDaysReach80Percent() + "")
                    .replace(":daysReachFull", result.getDaysReachFull() + "")
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

    private String calculateUsePencent(BigDecimal currentValue, BigDecimal limitValue) {
        return currentValue.divide(limitValue).setScale(BigDecimal.ROUND_UP).multiply(new BigDecimal(100)) + "%";
    }

    private int calculateDaysReach80Percent(BigDecimal burnRate, BigDecimal limitValue) {
        return limitValue.divide(burnRate).multiply(new BigDecimal(0.8)).setScale(BigDecimal.ROUND_UP).intValue();
    }

    private int calculateDaysReachFull(BigDecimal burnRate, BigDecimal limitValue) {
        return limitValue.divide(burnRate).setScale(BigDecimal.ROUND_UP).intValue();
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

}
