package com.newsagregator.crawler.webcrawlers;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class PageCrawler {
    public Set<String> getPages(String url, String className) throws IOException {
        return Jsoup.connect(url).get().getElementsByClass(className).stream()
                .map(element -> element.getElementsByTag("a").attr("href"))
                .collect(Collectors.toSet());
    }
    public Set<String> getPages(String url, String className,String filter) throws IOException {
        return Jsoup.connect(url).get().getElementsByClass(className).parallelStream()
                .map(element -> element.getElementsByTag("a").attr("href"))
                .filter(s -> s.startsWith(filter))
                .collect(Collectors.toSet());
    }
}
