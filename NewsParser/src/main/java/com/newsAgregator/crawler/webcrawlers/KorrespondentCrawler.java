package com.newsAgregator.crawler.webcrawlers;

import com.newsAgregator.crawler.Crawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class KorrespondentCrawler implements Crawler {
    private Document page;

    @Override
    public Set<String> getPages(int count, String url) {
        try {
            page = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadLinksForNewsOnPage(page,"article__title");
    }


    private synchronized Set<String> uploadLinksForNewsOnPage(Document pageWithAllNews, String classNameWithNews) {
        return pageWithAllNews.getElementsByClass(classNameWithNews).parallelStream()
                .map(element -> element.getElementsByTag("a").attr("href"))
//                .filter(url->url.startsWith("https://korrespondent.net/"))
                .collect(Collectors.toSet());
    }

}
