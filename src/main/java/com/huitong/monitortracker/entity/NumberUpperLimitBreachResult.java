package com.huitong.monitortracker.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
public class NumberUpperLimitBreachResult {
    private String schemaName;
    private String tableName;
    private String columnName;
    private BigDecimal currentValue;
    private String limitValue;
    private BigDecimal burnRate;
    private String active;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String daysReach80Percent;
    private String usePercent;

}
