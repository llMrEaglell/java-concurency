package com.newsAgregator.crawler.webcrawlers;

import com.newsAgregator.crawler.Crawler;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.jsoup.Jsoup.connect;


public class UkrainePravdaCrawler implements Crawler {
    private static final String url = "https://www.pravda.com.ua/news/";

    @Override
    public Set<String> getPages(int count) {
        Set<String> pages = new HashSet<>(count);
        LocalDate date = LocalDate.now();
        do {
            StringBuilder urlWithPattern = getUrl(date);
            try {
                Document document = connect(urlWithPattern.toString()).get();
                Set<String> ulrsOnNewsList = uploadLinksForNewsOnPage(document, "article_header");
                addToList(pages, ulrsOnNewsList, count);
            } catch (IOException e) {
                e.printStackTrace();
            }
            date = date.minusDays(1);
        } while (pages.size() < count);
        return pages;
    }

    private StringBuilder getUrl(LocalDate date) {
        StringBuilder urlWithPattern = new StringBuilder();
        urlWithPattern.append(url);
        urlWithPattern.append("date_");
        urlWithPattern.append(date.getDayOfMonth());
        urlWithPattern.append(date.getMonthValue());
        urlWithPattern.append(date.getYear());
        return urlWithPattern;
    }

    private Set<String> uploadLinksForNewsOnPage(Document pageWithAllNews, String classNameWithNews) {
        return pageWithAllNews.body()
                .getElementsByClass(classNameWithNews).stream()
                .map(element -> element.getElementsByTag("a").attr("href"))
                .filter(url -> url.startsWith("/news"))
                .collect(Collectors.toSet());
    }
    private void addToList(Set<String> source, Set<String> urls, int maxSize) {
        urls.stream().takeWhile(url -> source.size() != maxSize).forEach(source::add);
    }

}
