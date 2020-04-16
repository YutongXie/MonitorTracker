package com.huitong.monitortracker.executor;

import com.huitong.monitortracker.entity.MonitorTrackerJobDetailConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class MonitorTrackerApplicationContextAware implements ApplicationContextAware {
    private static final String YAML_PROPERTY_FILE = "application.yml";
    private static ApplicationContext context;
    private static YamlPropertiesFactoryBean yamlFactoryBean;
    private MonitorTrackerApplicationContextAware(){}
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        yamlFactoryBean = new YamlPropertiesFactoryBean();
        yamlFactoryBean.setResources(new ClassPathResource(YAML_PROPERTY_FILE));
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static String getProperty(String propertyName) {
        return yamlFactoryBean.getObject().getProperty(propertyName);
    }
}
