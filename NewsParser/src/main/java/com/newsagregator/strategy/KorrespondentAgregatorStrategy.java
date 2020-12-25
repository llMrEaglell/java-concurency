package com.newsagregator.strategy;

import com.newsagregator.FailUrlProcessor;
import com.newsagregator.FailureTaskProcessing;
import com.newsagregator.NewsRepository;
import com.newsagregator.crawler.webcrawlers.PageCrawler;
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
import java.util.stream.Collectors;

import static java.lang.System.*;

public class KorrespondentAgregatorStrategy implements AgregatorStrategy {
    private static final String BASE_URL = "https://korrespondent.net/all/";
    private static final String POST_ITEM_TITLE = "post-item__title";
    private static final String NEWS_CLASS = "article__title";
    private static final String ITEM_BIG_PHOTO_IMG = "post-item__big-photo-img";
    private static final String POST_ITEM_TEXT = "post-item__text";
    private static final String WITH_TIME_CLASS = "post-item__info";
    private static final String POST_ITEM_TAGS_ITEM = "post-item__tags-item";
    private static final int COUNT_RESOURCE_SEMAPHORE = 15;
    private static final int schendulerPeriodOnMinutes = 1;

    private final Semaphore semaphore;
    private static LocalDate date = LocalDate.now();
    private static AtomicInteger pageCounter = new AtomicInteger(1);

    private final NewsRepository repository;
    private PageCrawler crawler;
    private Parser parser;

    private Set<String> failureURLS = new ConcurrentSkipListSet<>();

    public KorrespondentAgregatorStrategy(NewsRepository repository) {
        semaphore = new Semaphore(COUNT_RESOURCE_SEMAPHORE);
        this.repository = repository;
        crawler = new PageCrawler();
        parser = new KorrespondentNewsPageParser(
                POST_ITEM_TITLE, ITEM_BIG_PHOTO_IMG, POST_ITEM_TEXT, WITH_TIME_CLASS, POST_ITEM_TAGS_ITEM);
    }

    @Override
    public void parseAndSaveNews(int count) {
        Runnable task = new FailureTaskProcessing();
        Thread test = new Thread(task);
        test.setDaemon(true);
        test.start();
        FailUrlProcessor failureTaskProcessing = new FailUrlProcessor(failureURLS, schendulerPeriodOnMinutes, parser, repository);
        failureTaskProcessing.check();
        err.println("Start Crawling");
        Set<String> newsUrls = crawlingUrls(count);
        err.println("Start Parsing");
        Set<News> news = councurencyParse(newsUrls);
        err.println("Start saving");
        out.println("Successful:" + news.size());
        out.println("Failure:" + failureURLS.size());
        repository.saveNews(news);
        failureTaskProcessing.shutdown();
    }

    private Set<News> councurencyParse(Set<String> newsUrls) {
        err.println("Start paring");
        ExecutorService service = Executors.newCachedThreadPool();
        List<Future<News>> futures = getFutureNews(newsUrls, service);
        err.println("Start parse result");
        Set<News> news = futures.parallelStream()
                .map(this::getNews)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(CopyOnWriteArraySet::new));
        service.shutdown();
        await(service);
        return news;
    }

    private void await(ExecutorService service) {
        try {
            if (!service.awaitTermination(40, TimeUnit.SECONDS))
                err.println("Threads didn't finish in 20 seconds!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private News getNews(Future<News> newsFuture) {
        try {
            News obj = newsFuture.get(30, TimeUnit.SECONDS);
            if (obj != null)
                return obj;
        } catch (ArrayIndexOutOfBoundsException | InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        return null;
    }

    private CopyOnWriteArrayList<Future<News>> getFutureNews(Set<String> newsUrls, ExecutorService service) {
        return newsUrls.stream()
                .map(s -> service.submit(() -> {
                    semaphore.acquire();
                    Document doc = connectToPage(s);
                    semaphore.release();
                    if (doc != null) {
                        return parser.parsePage(doc);
                    } else {
                        FailureTaskProcessing.check();
                        failureURLS.add(s);
                        return null;
                    }
                }))
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
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
                date.getMonth().toString().toLowerCase(),
                date.getDayOfMonth(),
                pageCounter.get());
        return urlPage;
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
