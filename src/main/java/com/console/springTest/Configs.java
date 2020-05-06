package com.console.springTest;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class Configs {

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dsb = DataSourceBuilder.create();
        dsb.driverClassName("org.postgresql.Driver");
        dsb.url("jdbc:postgresql://localhost:5432/postres");
        dsb.username("postgres");
        dsb.password("postgres");
        return dsb.build();
    }
}
