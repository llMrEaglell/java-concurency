package com.newsagregator.url.generator;

import com.newsagregator.site.properties.loader.NewsSiteProperties;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseURLGenerator implements NewsSiteURLGenerator {
    protected final NewsSiteProperties properties;
    protected LocalDate date;
    protected final AtomicInteger pageCounter;

    protected BaseURLGenerator(NewsSiteProperties properties, LocalDate date, int startCounterValue){
        this.properties = properties;
        this.date = date;
        this.pageCounter = new AtomicInteger(startCounterValue);
    }

    @Override
    public void minusDay() {
        date = date.minusDays(1);
    }

    @Override
    public void setCounterToStart() {
        pageCounter.set(1);
    }

    @Override
    public int nextPageCounter() {
        return pageCounter.incrementAndGet();
    }
}
