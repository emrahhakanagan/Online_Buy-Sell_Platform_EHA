package com.agan.onlinebuysellplatform.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

//    @Bean
//    public SpringLiquibase liquibase() {
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setShouldRun(false);
//        return liquibase;
//    }

    @Bean
    public LiquibaseProperties liquibaseProperties() {
        LiquibaseProperties properties = new LiquibaseProperties();
        properties.setEnabled(false);
        return properties;
    }
}
