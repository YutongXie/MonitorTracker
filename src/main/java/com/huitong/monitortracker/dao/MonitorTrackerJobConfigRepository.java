package com.huitong.monitortracker.dao;

import com.huitong.monitortracker.entity.MonitorTrackerJobConfigs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitorTrackerJobConfigRepository extends JpaRepository<MonitorTrackerJobConfigs, Long> {
}
