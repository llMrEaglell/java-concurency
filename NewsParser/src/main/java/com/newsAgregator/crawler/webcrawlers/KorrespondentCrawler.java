package com.newsAgregator.crawler.webcrawlers;

import com.newsAgregator.crawler.Crawler;
import newsAgregator.dateParser.DateParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KorrespondentCrawler implements Crawler {
    private static final String BASE_URL = "https://korrespondent.net/all/";

    @Override
    public Set<String> getPages(int count) {
        Set<String> pages = new HashSet<>(count);
        LocalDate date = LocalDate.now();
        AtomicInteger p = new AtomicInteger(1);
        do {
            StringBuilder stringBuilder = urlLoad(date, p);
            try {
                Document doc = Jsoup.connect(stringBuilder.toString()).get();
                Set<String> urls = uploadLinksForNewsOnPage(doc, "article__title");
                if (urls.isEmpty()) {
                    date = date.minusDays(1);
                    p.set(1);
                } else p.incrementAndGet();
                addToList(pages, urls, count);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (pages.size() < count);
        return pages;
    }

    private StringBuilder urlLoad(LocalDate date, AtomicInteger p) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BASE_URL);
        stringBuilder.append('/');
        stringBuilder.append(date.getYear());
        stringBuilder.append('/');
        stringBuilder.append(DateParser.getMonth(date.getMonthValue()));
        stringBuilder.append('/');
        stringBuilder.append(date.getDayOfMonth());
        stringBuilder.append("/p");
        stringBuilder.append(p.get());
        stringBuilder.append("/print/");
        return stringBuilder;
    }

    private synchronized Set<String> uploadLinksForNewsOnPage(Document pageWithAllNews, String classNameWithNews) {
        return pageWithAllNews.getElementsByClass(classNameWithNews).parallelStream()
                .map(element -> element.getElementsByTag("a").attr("href"))
                .filter(url->url.startsWith("https://korrespondent.net/"))
                .collect(Collectors.toSet());
    }
    private synchronized void addToList(Set<String> source, Set<String> urls, int maxSize) {
        urls.parallelStream().takeWhile(url -> source.size() != maxSize).forEach(source::add);
    }

}
