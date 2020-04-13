package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.entity.NumberUpperLimitBreachMetaData;

import java.util.List;

public interface NumberUpperLimitBreachInputProcessorDAO {
    List<NumberUpperLimitBreachMetaData> getMetaData();
}
