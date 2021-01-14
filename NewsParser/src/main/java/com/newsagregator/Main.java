package com.newsagregator;


import com.newsagregator.strategy.AggregatorStrategy;
import com.newsagregator.strategy.KorrespondentAggregatorStrategy;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        DataSource dataSource = getDataSource();
        Flyway flyway = createFlyway(dataSource);
        NewsRepository newsRepository = new NewsRepository(dataSource);
        AggregatorStrategy strategy = new KorrespondentAggregatorStrategy(newsRepository);
        flyway.migrate();
        strategy.parseAndSaveNews(100);
    }

    private static Flyway createFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .load();
    }

    private static DataSource getDataSource() throws IOException {
        Properties cfg = loadProperties();
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPassword(cfg.getProperty("jdbc.password"));
        hikariConfig.setUsername(cfg.getProperty(("jdbc.username")));
        hikariConfig.setJdbcUrl(cfg.getProperty(("jdbc.url")));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(cfg.getProperty(("jdbc.pool.max.connection"))));

        return new HikariDataSource(hikariConfig);
    }

    private static Properties loadProperties() throws IOException {
        InputStream resource = Main.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(resource);
        return properties;
    }
}
