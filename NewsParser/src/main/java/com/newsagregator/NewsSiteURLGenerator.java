package com.newsagregator;

public interface NewsSiteURLGenerator {
    String getUrl();
    void minusDay();
    void setCounterToStart();
    int nextPageCounter();
}
