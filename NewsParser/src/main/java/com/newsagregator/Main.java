package com.newsagregator;


import com.newsagregator.crawler.webcrawlers.MainPageCrawler;
import com.newsagregator.crawler.webcrawlers.PageCrawler;
import com.newsagregator.strategy.AggregatorStrategy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        DBConfig dbConfig = new DBConfig("application.properties");
        ExecutorService service = Executors.newCachedThreadPool();
        PageCrawler crawler = new MainPageCrawler();
        NewsInitializer newsInitializer = new NewsInitializer(dbConfig.getNewsRepository(), crawler);
        for (NewsSites s : NewsSites.values()) {
            AggregatorStrategy strategy = newsInitializer.getNewsStrategy(s);
            service.submit(() -> strategy.parseAndSaveNews(100));
        }
        service.shutdown();
    }

}
