package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.entity.NumberUpperLimitBreachStatistic;

import java.util.List;

public interface NumberUpperLimitBreachInputProcessorDAO {
    List<NumberUpperLimitBreachStatistic> getStatistic();
}
