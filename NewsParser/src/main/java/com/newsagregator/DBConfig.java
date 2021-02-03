package com.newsagregator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.util.Properties;

public class DBConfig {
    private DataSource dataSource;
    private Flyway flyway;
    private NewsRepository newsRepository;

    public DBConfig(String nameDBPropertiesFile) {
        dataSource = getDataSource(nameDBPropertiesFile);
        flyway = createFlyway();
        flyway.migrate();
        newsRepository = new NewsRepository(dataSource);
    }

    private Flyway createFlyway() {
        return Flyway.configure()
                .dataSource(dataSource)
                .load();
    }

    private DataSource getDataSource(String nameDBPropertiesFile) {
        Properties cfg = PropertiesLoader.load(nameDBPropertiesFile);
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPassword(cfg.getProperty("jdbc.password"));
        hikariConfig.setUsername(cfg.getProperty(("jdbc.username")));
        hikariConfig.setJdbcUrl(cfg.getProperty(("jdbc.url")));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(cfg.getProperty(("jdbc.pool.max.connection"))));

        return new HikariDataSource(hikariConfig);
    }

    public NewsRepository getNewsRepository() {
        return newsRepository;
    }
}
