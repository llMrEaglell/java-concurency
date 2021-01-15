package com.newsagregator;

import java.time.LocalDate;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StranaURLGenerator implements NewsSiteURLGenerator {
    private final NewsSiteProperties properties;
    private LocalDate date;
    private final AtomicInteger pageCounter;

    public StranaURLGenerator(NewsSiteProperties properties, LocalDate date, int startCounterValue) {
        this.properties = properties;
        this.date = date;
        this.pageCounter = new AtomicInteger(startCounterValue);
    }

    @Override
    public String getUrl() {
        String urlPage;
        urlPage = String.format("%s/day=%d-%d-%d/page-%d.html",
                properties.getBaseURL(),
                date.getYear(),
                date.getMonthValue(),
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
                    } else if (s.startsWith("/")) {
                        String newUrl = properties.getFilter();
                        return newUrl + s;
                    } else {
                        return s;
                    }
                })
                .collect(Collectors.toSet());
    }
}
