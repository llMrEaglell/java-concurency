package com.newsagregator;

import com.newsagregator.news.News;
import com.newsagregator.parsers.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static java.lang.System.err;

public class FailUrlProcessor {
    private final int timeStart;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Set<String> urls;
    private Parser parser;
    private NewsRepository repository;

    public FailUrlProcessor(Set<String> urls, int timeOnMinutes, Parser parser, NewsRepository repository) {
        this.repository = repository;
        this.timeStart = timeOnMinutes;
        this.urls = urls;
        this.parser = parser;
    }

    public void check() {
        Set<String> failure = new ConcurrentSkipListSet<>();
        Set<News> newsSet = new HashSet<>();
        final Runnable task = () -> {
            if (!urls.isEmpty()) {
                err.println("Start schedule task");
                for (String s : urls) {
                    News news = parser.parsePage(connectToPage(s));
                    newsSet.add(news);
                }
                repository.saveNews(newsSet);
                urls.clear();
                urls.addAll(failure);
            }else{
                err.println("Failure urls not found");
            }
        };
        scheduler.scheduleAtFixedRate(task, timeStart, timeStart, TimeUnit.MINUTES);
    }

    public void shutdown(){
        scheduler.shutdown();
        try {
            scheduler.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Document connectToPage(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                    .referrer("http://www.google.com").get();
        } catch (IOException e) {
            err.println("Can't connect to:" + url);
        }
        return doc;
    }
}
