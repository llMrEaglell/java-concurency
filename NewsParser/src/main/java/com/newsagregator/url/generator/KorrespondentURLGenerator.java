package com.newsagregator.url.generator;

import com.newsagregator.site.properties.loader.NewsSiteProperties;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class KorrespondentURLGenerator extends BaseURLGenerator implements NewsSiteURLGenerator {
    public KorrespondentURLGenerator(NewsSiteProperties properties, LocalDate date, int startCounterValue) {
        super(properties, date, startCounterValue);
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
