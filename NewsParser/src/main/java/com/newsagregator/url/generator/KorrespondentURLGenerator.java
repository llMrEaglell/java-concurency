package com.newsagregator.url.generator;

import com.newsagregator.site.properties.loader.NewsSiteProperties;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class KorrespondentURLGenerator implements NewsSiteURLGenerator {
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

    @Override
    public Set<String> fixShortURL(Set<String> urls) {
        return urls.stream()
                .map(s -> {
                    if (s.startsWith(properties.getFilter())) {
                        return s;
                    } else {
                        String newUrl = properties.getFilter();
                        return newUrl + s;
                    }
                })
                .collect(Collectors.toSet());
    }


}
