package com.bagautdinov.config;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:persistence.properties")
public class PersistenceConfig {

    private final Environment env;

    public PersistenceConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(env.getRequiredProperty("spring.datasource.url"));
        ds.setUsername(env.getRequiredProperty("spring.datasource.username"));
        ds.setPassword(env.getRequiredProperty("spring.datasource.password"));
        ds.setDriverClassName(env.getRequiredProperty("spring.datasource.driver-class-name"));
        return ds;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
        sf.setDataSource(dataSource);
        sf.setPackagesToScan("com.bagautdinov.model");

        Properties props = new Properties();
        props.setProperty("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql", "true"));
        props.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql", "true"));
        props.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto", "update"));

        sf.setHibernateProperties(props);
        return sf;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
}