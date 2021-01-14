package com.newsagregator.strategy;

import com.newsagregator.*;
import com.newsagregator.crawler.webcrawlers.PageCrawler;
import com.newsagregator.news.News;
import com.newsagregator.parsers.KorrespondentNewsPageParser;
import com.newsagregator.parsers.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static java.lang.System.err;
import static java.lang.System.out;

public class NewsAggregator implements AggregatorStrategy {
    private final NewsSiteProperties properties;
    private static final int SCHEDULER_PERIOD_ON_MINUTES = 1;

    private final Semaphore semaphore;

    private final NewsSiteURLGenerator urlGenerator;
    private final NewsRepository repository;

    private PageCrawler crawler;
    private Parser parser;
    private Set<String> failureURLS = new ConcurrentSkipListSet<>();

    public NewsAggregator(NewsRepository repository, NewsSiteProperties properties, NewsSiteURLGenerator urlGenerator) {
        int connectionLimit = properties.getConnectionLimit();
        semaphore = new Semaphore(connectionLimit);
        this.repository = repository;
        this.properties = properties;
        this.urlGenerator = urlGenerator;
        crawler = new PageCrawler();
        parser = new KorrespondentNewsPageParser(
                properties.getPostItemTitle(), properties.getItemBigPhotoIMG(),
                properties.getPostItemText(), properties.getTimeClass(), properties.getTagsItemClass());
    }

    @Override
    public void parseAndSaveNews(int count) {
        Runnable task = new FailureTaskProcessing();
        Thread test = new Thread(task);
        test.setDaemon(true);
        test.start();

        FailUrlProcessor failureTaskProcessing = new FailUrlProcessor(failureURLS, SCHEDULER_PERIOD_ON_MINUTES, parser, repository);
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
            News obj = newsFuture.get(1, TimeUnit.MINUTES);
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
        url = urlGenerator.getUrl();
        String finalUrl = url;
        Document page = connectToPage(finalUrl);
        if (page != null) {
            Set<String> urls = crawler.getPages(page,properties.getNewsClass(), properties.getFilter());
            if (urls.isEmpty()) {
                urlGenerator.minusDay();
                urlGenerator.setCounterToStart();
            } else {
                urlGenerator.nextPageCounter();
                addToList(newsUrls, urls, count);
            }
        }
    }


    void addToList(Set<String> source, Set<String> urls, int maxSize) {
        urls.stream().takeWhile(url -> source.size() != maxSize).forEach(source::add);
    }


    private Document connectToPage(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .timeout(10000)
                    .referrer("http://www.google.com").get();
        } catch (IOException e) {
            err.println("Can't connect to:" + url);
        }
        return doc;
    }
}
