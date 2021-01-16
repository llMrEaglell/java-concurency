package com.newsagregator;


import com.newsagregator.crawler.webcrawlers.MainPageCrawler;
import com.newsagregator.crawler.webcrawlers.PageCrawler;
import com.newsagregator.parsers.Parser;
import com.newsagregator.parsers.StranaNewsPageParser;
import com.newsagregator.strategy.AggregatorStrategy;
import com.newsagregator.strategy.NewsAggregator;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        DataSource dataSource = getDataSource();
        Flyway flyway = createFlyway(dataSource);
        NewsRepository newsRepository = new NewsRepository(dataSource);
        flyway.migrate();

        PageCrawler crawler = new MainPageCrawler();
//        NewsSiteProperties properties = new KorrepsondentProperties("korrespondent.properties");
//        NewsSiteURLGenerator generator = new KorrespondentURLGenerator(properties, LocalDate.now(), 1);
//        Parser parser = new KorrespondentNewsPageParser(
//                properties.getPostItemTitle(), properties.getItemBigPhotoIMG(),
//                properties.getPostItemText(), properties.getTimeClass(), properties.getTagsItemClass());
//        AggregatorStrategy strategy = new NewsAggregator(newsRepository, properties, generator,parser,crawler);
        NewsSiteProperties stranaProperties = new StranaProperties("strana.properties");
        NewsSiteURLGenerator stranaGenerator = new StranaURLGenerator(stranaProperties, LocalDate.now(), 1);
        Parser parser = new StranaNewsPageParser(
                stranaProperties.getPostItemTitle(), stranaProperties.getItemBigPhotoIMG(),
                stranaProperties.getPostItemText(), stranaProperties.getTimeClass(), stranaProperties.getTagsItemClass());
        AggregatorStrategy strategy = new NewsAggregator(newsRepository, stranaProperties, stranaGenerator, parser, crawler);

        strategy.parseAndSaveNews(100);


//        strategy.parseAndSaveNews(100);
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
