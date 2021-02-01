package com.newsagregator;

import com.newsagregator.crawler.webcrawlers.PageCrawler;
import com.newsagregator.parsers.KorrespondentNewsPageParser;
import com.newsagregator.parsers.Parser;
import com.newsagregator.parsers.StranaNewsPageParser;
import com.newsagregator.site.properties.loader.KorrepsondentProperties;
import com.newsagregator.site.properties.loader.NewsSiteProperties;
import com.newsagregator.site.properties.loader.StranaProperties;
import com.newsagregator.strategy.NewsAggregator;
import com.newsagregator.url.generator.KorrespondentURLGenerator;
import com.newsagregator.url.generator.NewsSiteURLGenerator;
import com.newsagregator.url.generator.StranaURLGenerator;

import java.time.LocalDate;

public class NewsInitializer {
    private final NewsRepository repository;
    private final PageCrawler crawler;

    public NewsInitializer(NewsRepository newsRepository, PageCrawler crawler) {
        repository = newsRepository;
        this.crawler = crawler;
    }

    public NewsAggregator getNewsStrategy(NewsSites site) {
        switch (site) {
            case KORRESPONDENT:
                return getKorrespondentNewsAggregator(site);
            case STRANA:
                return getStranaNewsAggregator(site);
            default:
                System.err.println("Strategy for this site not found:");
                return null;
        }
    }

    private NewsAggregator getStranaNewsAggregator(NewsSites site) {
        NewsSiteProperties properties = new StranaProperties(site.getPropertiesFile());
        NewsSiteURLGenerator generator = new StranaURLGenerator(properties, LocalDate.now(), 1);
        Parser parser = new StranaNewsPageParser(
                properties.getPostItemTitle(), properties.getItemBigPhotoIMG(),
                properties.getPostItemText(), properties.getTimeClass(), properties.getTagsItemClass());
        return new NewsAggregator(repository, properties, generator, parser, crawler);
    }

    private NewsAggregator getKorrespondentNewsAggregator(NewsSites site) {
        NewsSiteProperties properties = new KorrepsondentProperties(site.getPropertiesFile());
        NewsSiteURLGenerator generator = new KorrespondentURLGenerator(properties, LocalDate.now(), 1);
        Parser parser = new KorrespondentNewsPageParser(
                properties.getPostItemTitle(), properties.getItemBigPhotoIMG(),
                properties.getPostItemText(), properties.getTimeClass(), properties.getTagsItemClass());
        return new NewsAggregator(repository, properties, generator, parser, crawler);
    }
}
