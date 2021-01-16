package com.newsagregator.crawler.webcrawlers;

import org.jsoup.nodes.Document;

import java.util.Set;

public interface PageCrawler {
    Set<String> getPages(Document page,String blockWithArticles, String className);

    Set<String> getPages(Document page,String blockWithArticles, String className, String filter);
}
