package com.bugtracker.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@SpringBootApplication
@Configuration
public class FlywayConfig {
    public FlywayConfig(DataSource dataSource) {
        Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();

    }
}
