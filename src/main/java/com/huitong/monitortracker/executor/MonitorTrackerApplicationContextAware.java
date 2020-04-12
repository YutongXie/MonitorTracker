package com.huitong.monitortracker.executor;

import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class MonitorTrackerApplicationContextAware implements ApplicationContextAware {
    private static ApplicationContext context;

    private MonitorTrackerApplicationContextAware(){}
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
}
