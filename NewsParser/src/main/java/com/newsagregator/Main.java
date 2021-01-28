package com.newsagregator;


import com.newsagregator.crawler.webcrawlers.MainPageCrawler;
import com.newsagregator.crawler.webcrawlers.PageCrawler;
import com.newsagregator.parsers.KorrespondentNewsPageParser;
import com.newsagregator.parsers.Parser;
import com.newsagregator.parsers.StranaNewsPageParser;
import com.newsagregator.site.properties.loader.KorrepsondentProperties;
import com.newsagregator.site.properties.loader.NewsSiteProperties;
import com.newsagregator.site.properties.loader.StranaProperties;
import com.newsagregator.strategy.AggregatorStrategy;
import com.newsagregator.strategy.NewsAggregator;
import com.newsagregator.url.generator.KorrespondentURLGenerator;
import com.newsagregator.url.generator.NewsSiteURLGenerator;
import com.newsagregator.url.generator.StranaURLGenerator;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args){
        DBConfig dbConfig = new DBConfig("application.properties");
        ExecutorService service = Executors.newCachedThreadPool();
        PageCrawler crawler = new MainPageCrawler();

        NewsSiteProperties properties = new KorrepsondentProperties("korrespondent.properties");
        NewsSiteURLGenerator generator = new KorrespondentURLGenerator(properties, LocalDate.now(), 1);
        Parser parser = new KorrespondentNewsPageParser(
                properties.getPostItemTitle(), properties.getItemBigPhotoIMG(),
                properties.getPostItemText(), properties.getTimeClass(), properties.getTagsItemClass());
        AggregatorStrategy strategy = new NewsAggregator(dbConfig.getNewsRepository(), properties, generator, parser, crawler);

        NewsSiteProperties stranaProperties = new StranaProperties("strana.properties");
        NewsSiteURLGenerator stranaGenerator = new StranaURLGenerator(stranaProperties, LocalDate.now(), 1);
        Parser parser2 = new StranaNewsPageParser(
                stranaProperties.getPostItemTitle(), stranaProperties.getItemBigPhotoIMG(),
                stranaProperties.getPostItemText(), stranaProperties.getTimeClass(), stranaProperties.getTagsItemClass());
        AggregatorStrategy strategy2 = new NewsAggregator(dbConfig.getNewsRepository(), stranaProperties, stranaGenerator, parser2, crawler);
        service.submit(() -> strategy.parseAndSaveNews(10000));
        service.submit(() -> strategy2.parseAndSaveNews(10000));
        service.shutdown();
    }

}
