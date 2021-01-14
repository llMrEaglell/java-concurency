package com.newsagregator;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public interface NewsSiteURLGenerator {
    void generateUrls(int count, Set<String> newsUrls);
    void generateNextUrl(boolean isUrlHaveNews);
    String getUrl(NewsSiteProperties properties, LocalDate date, AtomicInteger pageCounter);
}
