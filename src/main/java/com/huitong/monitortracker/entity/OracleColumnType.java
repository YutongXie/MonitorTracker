package com.huitong.monitortracker.entity;

public enum OracleColumnType {
    NUMBER("NUMBER", "9.99...E+125", "9.99...E+"),
    FLOAT("FLOAT", "9.99...E+125", "9.99...E+"),
    BINARY_FLOAT("BINARY_FLOAT", "1.79769313486231E+308", ""),
    BINARY_DOUBLE("BINARY_DOUBLE", "3.40282E+38", "");

    private String name;
    private String maxValue;
    private String displayFormat;

    private OracleColumnType(String name, String maxValue, String displayFormat) {
        this.name = name;
        this.maxValue = maxValue;
        this.displayFormat = displayFormat;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

}
