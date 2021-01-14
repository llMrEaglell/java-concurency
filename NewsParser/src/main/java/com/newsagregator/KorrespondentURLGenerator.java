package com.newsagregator;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class KorrespondentURLGenerator implements NewsSiteURLGenerator{
    private final NewsSiteProperties properties;
    private LocalDate date;
    private final AtomicInteger pageCounter;

    public KorrespondentURLGenerator(NewsSiteProperties properties, LocalDate date, int startCounterValue) {
        this.properties = properties;
        this.date = date;
        this.pageCounter = new AtomicInteger(startCounterValue);
    }

    @Override
    public String getUrl() {
        String urlPage;
        urlPage = String.format("%s/%d/%s/%d/p%d/print/",
                properties.getBaseURL(),
                date.getYear(),
                date.getMonth().toString().toLowerCase(),
                date.getDayOfMonth(),
                pageCounter.get());
        return urlPage;
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
