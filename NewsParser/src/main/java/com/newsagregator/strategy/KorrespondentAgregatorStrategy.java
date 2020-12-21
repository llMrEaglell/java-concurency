package com.newsagregator.strategy;

import com.newsagregator.NewsRepository;
import com.newsagregator.crawler.webcrawlers.PageCrawler;
import com.newsagregator.dateParser.DateParser;
import com.newsagregator.news.News;
import com.newsagregator.parsers.KorrespondentNewsPageParser;
import com.newsagregator.parsers.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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

    private final NewsRepository repository;
    private PageCrawler crawler;
    private Parser parser;

    public KorrespondentAgregatorStrategy(NewsRepository repository) {
        this.repository = repository;
        crawler = new PageCrawler();
        parser = new KorrespondentNewsPageParser(
                POST_ITEM_TITLE, ITEM_BIG_PHOTO_IMG, textClasPOST_ITEM_TEXT, WITH_TIME_CLASS, POST_ITEM_TAGS_ITEM);
    }

    @Override
    public void parseAndSaveNews(int count) {
        err.println("Start Crawling");
        Set<String> newsUrls = crawlingUrls(count);
        err.println("Stop Crawling");
        Set<News> news = councurencyParse(newsUrls);
        err.println("Start saving");
        repository.saveNews(news);
    }

    private Set<News> councurencyParse(Set<String> newsUrls) {
        err.println("Start paring");
        ExecutorService service = Executors.newFixedThreadPool(10);
        List<Future<News>> futures = new CopyOnWriteArrayList<>();
        Set<News> news = new CopyOnWriteArraySet<>();
        newsUrls.forEach(s -> futures.add(service.submit(() -> parser.parsePage(s))));
        err.println("Start parse result");
        futures.parallelStream().forEach(newsFuture -> {
            try {
                news.add(newsFuture.get());
            } catch (ArrayIndexOutOfBoundsException | InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
        try {
            service.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        service.shutdown();
        return news;
    }

    private Set<String> crawlingUrls(int count) {
        Set<String> newsUrls = new HashSet<>();
        do {
            generateUrls(count, newsUrls);
        } while (newsUrls.size() < count);
        return newsUrls;
    }

    private void generateUrls(int count, Set<String> newsUrls) {
        String url;
        url = getUrl();
        String finalUrl = url;
        Document page = connectToPage(finalUrl);
        if (page != null) {
            Set<String> urls = crawler.getPages(page, NEWS_CLASS, "https://korrespondent.net/");
            if (urls.isEmpty()) {
                date = date.minusDays(1);
                pageCounter.set(1);
            } else {
                pageCounter.incrementAndGet();
                addToList(newsUrls, urls, count);
            }
            generateNextUrl(urls.isEmpty());
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

    private Document connectToPage(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).timeout(10000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
