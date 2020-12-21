package com.newsAgregator.crawler;


import java.io.IOException;
import java.util.Set;

public interface Crawler {
    Set<String> getPages(String url) throws IOException;
}
