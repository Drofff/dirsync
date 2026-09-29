package dev.mpalahuta.dirsync.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(@Value("${dirsync.datasource.path}") String dbPath) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:" + dbPath);
        // SQLite allows a single writer; more connections lead to SQLITE_BUSY errors.
        config.setMaximumPoolSize(1);
        return new HikariDataSource(config);
    }

}
