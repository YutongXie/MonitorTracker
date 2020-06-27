package com.huitong.monitortracker.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.huitong.monitortracker.dao.mybatis.output", sqlSessionFactoryRef = "outputSqlSessionFactory")
public class MybatisOutputDataSource {
    @Bean(name="outputSqlSessionFactory")
    public SqlSessionFactory businessSqlSessionFactory(@Qualifier("outputProcessorDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        return sessionFactoryBean.getObject();
    }
    @Bean(name="outputSqlSessionTemplate")
    public SqlSessionTemplate businessSqlSessionTemplate(@Qualifier("outputSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
