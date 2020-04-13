package com.huitong.monitortracker.entity;

public class NumberUpperLimitBreachMetaData {
    private String schemaName;
    private String tableName;
    private String columnName;
    private String dataType;
    private Long dataLength;
    private Long dataPrecision;
    private Long dataScale;

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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Long getDataLength() {
        return dataLength;
    }

    public void setDataLength(Long dataLength) {
        this.dataLength = dataLength;
    }

    public Long getDataPrecision() {
        return dataPrecision;
    }

    public void setDataPrecision(Long dataPrecision) {
        this.dataPrecision = dataPrecision;
    }

    public Long getDataScale() {
        return dataScale;
    }

    public void setDataScale(Long dataScale) {
        this.dataScale = dataScale;
    }
}
