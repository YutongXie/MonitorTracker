package com.huitong.monitortracker.entity;

public enum NumberUpperLimitBreachConstants {

    USE_PERCENT_DEFAULT_VALUE("USE_PERCENT_DEFAULT_VALUE", "0.00%"),
    FORK_JOIN_EXECUTOR_THRESHOLD("FORK_JOIN_EXECUTOR_THRESHOLD", "2"),
    WARNING_LINE_THRESHOLD_USE_PERCENT("WARNING_LINE_THRESHOLD_USE_PERCENT", "80"),
    WARNING_LINE_DAYS_REACH_TO_THRESHOLD_PERCENT("WARNING_LINE_DAYS_REACH_TO_THRESHOLD_PERCENT", "< 15"),
    DAYS_REACH_TO_80_PERCENT_MORE_THAN_90("DAYS_REACH_TO_80_PERCENT_MORE_THAN_90", "> 90"),
    DAYS_REACH_TO_80_PERCENT_MORE_THAN_60("DAYS_REACH_TO_80_PERCENT_MORE_THAN_60", "> 60"),
    DAYS_REACH_TO_80_PERCENT_MORE_THAN_30("DAYS_REACH_TO_80_PERCENT_MORE_THAN_30", "> 30"),
    DAYS_REACH_TO_80_PERCENT_MORE_THAN_15("DAYS_REACH_TO_80_PERCENT_MORE_THAN_15", "> 15"),
    DAYS_REACH_TO_80_PERCENT_LESS_THAN_15("DAYS_REACH_TO_80_PERCENT_LESS_THAN_15", "< 15");

    private String name;
    private String value;

    NumberUpperLimitBreachConstants(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getValueAsNumber() {
        return Integer.parseInt(value);
    }
}
