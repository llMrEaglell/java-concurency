package com.newsAgregator.crawler;


import java.util.Set;

public interface Crawler {
    Set<String> getPages(int count,String url);
}
