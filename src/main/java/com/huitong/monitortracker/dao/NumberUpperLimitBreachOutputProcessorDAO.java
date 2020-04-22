package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;

import java.util.List;

public interface NumberUpperLimitBreachOutputProcessorDAO {
    List<NumberUpperLimitBreachResult> getLastResult();
    void inactiveLastResult(String schemaName);
    void save(List<NumberUpperLimitBreachResult> resultList);
}
