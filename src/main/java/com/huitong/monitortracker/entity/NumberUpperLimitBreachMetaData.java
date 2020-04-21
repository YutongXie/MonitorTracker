package com.huitong.monitortracker.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NumberUpperLimitBreachMetaData {
    private String schemaName;
    private String tableName;
    private String columnName;
    private String dataType;
    private Long dataLength;
    private Long dataPrecision;
    private Long dataScale;

}
