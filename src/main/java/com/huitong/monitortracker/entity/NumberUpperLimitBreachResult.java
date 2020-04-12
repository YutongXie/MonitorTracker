package com.huitong.monitortracker.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NumberUpperLimitBreachResult {
    private String schemaName;
    private String tableName;
    private String columnName;
    private BigDecimal currentValue;
    private BigDecimal limitValue;
    private BigDecimal burnRate;
    private String active;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int daysReach80Percent;
    private int daysReachFull;
    private String usePercent;

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public BigDecimal getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(BigDecimal limitValue) {
        this.limitValue = limitValue;
    }

    public BigDecimal getBurnRate() {
        return burnRate;
    }

    public void setBurnRate(BigDecimal burnRate) {
        this.burnRate = burnRate;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public int getDaysReach80Percent() {
        return daysReach80Percent;
    }

    public void setDaysReach80Percent(int daysReach80Percent) {
        this.daysReach80Percent = daysReach80Percent;
    }

    public int getDaysReachFull() {
        return daysReachFull;
    }

    public void setDaysReachFull(int daysReachFull) {
        this.daysReachFull = daysReachFull;
    }

    public String getUsePercent() {
        return usePercent;
    }

    public void setUsePercent(String usePercent) {
        this.usePercent = usePercent;
    }
}
