package com.newsAgregator.crawler.webcrawlers;

import com.newsAgregator.crawler.Crawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class KorrespondentCrawler implements Crawler {

    private static final int NUM_OF_THREADS = Runtime.getRuntime().availableProcessors();
    private Document page;

    @Override
    public ConcurrentSkipListSet<String> getPages(int count, String url) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool(NUM_OF_THREADS);
//        AtomicReference<ConcurrentSkipListSet<String>> set = new AtomicReference<>();
        try {
            openDocument(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ExecutorService pool = Executors.newCachedThreadPool();
        return pool.submit(this::getUrls).get();


//        pool.submit(()->{
//            int i =0;
//            return i;
//        }).get();
//        return set.get();
    }

    private void openDocument(String url) throws IOException {
        this.page = Jsoup.connect(url).get();
    }

    private ConcurrentSkipListSet<String> getUrls() {
        return page.getElementsByClass("article__title").parallelStream()
                .map(element -> element.getElementsByTag("a").attr("href"))
                .filter(urlp -> urlp.startsWith("https://korrespondent.net/"))
                .collect(Collectors.toCollection(ConcurrentSkipListSet::new));
    }
}
