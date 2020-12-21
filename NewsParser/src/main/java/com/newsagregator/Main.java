package com.newsAgregator;


import com.newsAgregator.strategy.AgregatorStrategy;
import com.newsAgregator.strategy.KorrespondentAgregatorStrategy;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = getDataSource();
        Flyway flyway = createFlyway(dataSource);
        NewsRepository newsRepository = new NewsRepository(dataSource);
        AgregatorStrategy strategy = new KorrespondentAgregatorStrategy(newsRepository);
        flyway.migrate();
        strategy.parseAndSaveNews(10000);
    }

    private static Flyway createFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .load();
    }

    private static DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPassword(System.getenv("jdbc.password"));
        hikariConfig.setUsername(System.getenv("jdbc.username"));
        hikariConfig.setJdbcUrl(System.getenv("jdbc.url"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(System.getenv("jdbc.pool.max.connection")));

        return new HikariDataSource(hikariConfig);
    }

}
