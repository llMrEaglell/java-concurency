package com.newsagregator.crawler.webcrawlers;

import org.jsoup.nodes.Document;

import java.util.Set;
import java.util.stream.Collectors;

public class PageCrawler {
    public Set<String> getPages(Document page, String className){
        return page.getElementsByClass(className).stream()
                .map(element -> element.getElementsByTag("a").attr("href"))
                .collect(Collectors.toSet());
    }
    public Set<String> getPages(Document page, String className,String filter){
        return page.getElementsByClass(className).parallelStream()
                .map(element -> element.getElementsByTag("a").attr("href"))
                .filter(s -> s.startsWith(filter))
                .collect(Collectors.toSet());
    }
}
