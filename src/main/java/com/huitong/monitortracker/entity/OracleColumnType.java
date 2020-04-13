package com.huitong.monitortracker.entity;

public enum OracleColumnType {
    NUMBER("NUMBER", ""),
    FLOAT("FLOAT", ""),
    BINARY_FLOAT("BINARY_FLOAT", ""),
    BINARY_DOUBLE("BINARY_DOUBLE", "");

    private String name;
    private String maxValue;
    private OracleColumnType(String name, String maxValue) {
        this.name = name;
        this.maxValue = maxValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

}
