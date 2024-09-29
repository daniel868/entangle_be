package org.example.entablebe.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class TransactionalConfig {
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    public TransactionalConfig(DataSource dataSource, EntityManagerFactory entityManagerFactory) {
        this.dataSource = dataSource;
        this.entityManagerFactory = entityManagerFactory;
    }


    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource);
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
