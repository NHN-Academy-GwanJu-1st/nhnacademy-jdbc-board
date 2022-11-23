package com.nhnacademy.config;

import com.p6spy.engine.spy.P6DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@MapperScan(basePackages = "**.mapper.**",  sqlSessionFactoryRef = "sqlSessionFactoryBean")
public class DBConfig {

    @Bean
    public DataSource dataSource(){
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/nhnboard");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("1q2w3e4r");
        basicDataSource.setInitialSize(2);
        basicDataSource.setMaxTotal(10);
        return basicDataSource;
    }

    @Bean
    public DataSource logDataSource(){
        return new P6DataSource(dataSource());
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(logDataSource());
        sessionFactory.setMapperLocations(resolver.getResources("classpath:**/mapper/*.xml"));
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
