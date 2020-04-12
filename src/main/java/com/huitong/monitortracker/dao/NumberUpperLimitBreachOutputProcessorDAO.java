package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.entity.NumberUpperLimitBreachResult;

import java.util.List;

public interface NumberUpperLimitBreachOutputProcessorDAO {
    List<NumberUpperLimitBreachResult> getLastResult();
    void inactiveLastResult();
    void save(String[] sql);
}
