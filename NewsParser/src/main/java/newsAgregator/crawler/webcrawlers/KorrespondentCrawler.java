package newsAgregator.crawler.webcrawlers;

import newsAgregator.crawler.Crawler;
import newsAgregator.dateParser.DateParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class KorrespondentCrawler implements Crawler {
    private static final String BASE_URL = "https://korrespondent.net/all/";

    @Override
    public List<String> getPages(int count) {
        List<String> pages = new ArrayList<>(count);
        LocalDate date = LocalDate.now();
        AtomicInteger p = new AtomicInteger(1);
        do {
            StringBuilder stringBuilder = urlLoad(date, p);
            try {
                Document doc = Jsoup.connect(stringBuilder.toString()).get();
                List<String> urls = uploadLinksForNewsOnPage(doc, "article__title");
                if (urls.isEmpty()) {
                    date = date.minusDays(1);
                    p.set(1);
                } else p.incrementAndGet();
                addToList(pages, urls, count);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (pages.size() < count);
        return pages;
    }

    private StringBuilder urlLoad(LocalDate date, AtomicInteger p) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BASE_URL);
        stringBuilder.append('/');
        stringBuilder.append(date.getYear());
        stringBuilder.append('/');
        stringBuilder.append(DateParser.getMonth(date.getMonthValue()));
        stringBuilder.append('/');
        stringBuilder.append(date.getDayOfMonth());
        stringBuilder.append("/p");
        stringBuilder.append(p.get());
        stringBuilder.append("/print/");
        return stringBuilder;
    }
}
