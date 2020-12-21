package com.newsAgregator;

import com.newsAgregator.crawler.webcrawlers.PageCrawler;
import com.newsAgregator.dateParser.DateParser;
import com.newsAgregator.news.News;
import com.newsAgregator.parsers.KorrespondentNewsPageParser;
import com.newsAgregator.parsers.Parser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.*;

public class KorrespondentAgregatorStrategy implements AgregatorStrategy {
    private static final String BASE_URL = "https://korrespondent.net/all/";
    private static final String POST_ITEM_TITLE = "post-item__title";
    private static final String NEWS_CLASS = "article__title";
    private static final String ITEM_BIG_PHOTO_IMG = "post-item__big-photo-img";
    private static final String textClasPOST_ITEM_TEXT = "post-item__text";
    private static final String WITH_TIME_CLASS = "post-item__info";
    private static final String POST_ITEM_TAGS_ITEM = "post-item__tags-item";
    private static LocalDate date = LocalDate.now();
    private static AtomicInteger pageCounter = new AtomicInteger(1);

    private PageCrawler crawler;
    private Parser parser;

    public KorrespondentAgregatorStrategy() {
        crawler = new PageCrawler();
        parser = new KorrespondentNewsPageParser(
                POST_ITEM_TITLE, ITEM_BIG_PHOTO_IMG, textClasPOST_ITEM_TEXT, WITH_TIME_CLASS, POST_ITEM_TAGS_ITEM);
    }

    @Override
    public void getNews(int count) {
        err.println("Start Crawling");
        Set<String> newsUrls = crawlingUrls(count);
        err.println("Stop Crawling");
        List<News> news = councurencyParse(newsUrls);
        out.println(news.size());
    }

    private List<News> councurencyParse(Set<String> newsUrls) {
        err.println("Start paring");
        ExecutorService service = Executors.newFixedThreadPool(10);
        List<Future<News>> futures = new CopyOnWriteArrayList<>();
        List<News> news = new ArrayList<>();
        newsUrls.forEach(s -> futures.add(service.submit(() -> parser.parsePage(s))));
        err.println("Start parse result");
        futures.parallelStream().forEach(newsFuture -> {
            try {
                news.add(newsFuture.get());
            } catch (InterruptedException | ArrayIndexOutOfBoundsException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        service.shutdown();
        return news;
    }

    private Set<String> crawlingUrls(int count) {
        Set<String> newsUrls = new HashSet<>();
        String url;
        do {
            generateUrls(count, newsUrls);
        } while (newsUrls.size() < count);
        return newsUrls;
    }

    private void generateUrls(int count, Set<String> newsUrls) {
        String url;
        try {
            url = getUrl();
            String finalUrl = url;
            err.println(url + " " + Thread.currentThread().getName());
            Set<String> urls = crawler.getPages(finalUrl, NEWS_CLASS, "https://korrespondent.net/");
            if (urls.isEmpty()) {
                date = date.minusDays(1);
                pageCounter.set(1);
            } else {
                pageCounter.incrementAndGet();
                addToList(newsUrls, urls, count);
            }
            generateNextUrl(urls.isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateNextUrl(boolean isUrlHaveNews) {
        if (!isUrlHaveNews) {
            pageCounter.incrementAndGet();
        } else {
            date = date.minusDays(1);
            pageCounter.set(1);
        }
    }

    void addToList(Set<String> source, Set<String> urls, int maxSize) {
        urls.stream().takeWhile(url -> source.size() != maxSize).forEach(source::add);
    }

    private String getUrl() {
        String urlPage;
        urlPage = String.format("%s/%d/%s/%d/p%d/print/",
                BASE_URL,
                date.getYear(),
                DateParser.getMonth(date.getMonthValue()),
                date.getDayOfMonth(),
                pageCounter.get());
        return urlPage;
    }
}
