package crawler.webcrawlers;

import crawler.Crawler;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.jsoup.Jsoup.connect;


public class UkraineTrueCrawler implements Crawler {
    String url;

    public UkraineTrueCrawler(String url) {
        this.url = url;
    }

    @Override
    public List<String> getPages(int count) {
        List<String> pages = new ArrayList<>(count);
        LocalDate date = LocalDate.now();
        do {
            String urlWithPattern = url + "date_" + date.getDayOfMonth() + date.getMonthValue() + date.getYear();
            try {
                Document document = connect(urlWithPattern).get();
                List<String> ulrsOnNewsList = uploadLinksForNewsOnPage(document, "article_header");
                addToList(pages, ulrsOnNewsList, count);
            } catch (IOException e) {
                e.printStackTrace();
            }
            date = date.minusDays(1);
        } while (pages.size() < count);
        return pages;
    }

}
