package parsers;

import news.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import static java.lang.System.*;

public class UkrainePravdaNewsPageParser implements Parser {

    private static final String baseUrl = "https://www.pravda.com.ua/";
    private static final String titleClass = "post_title";
    private static final String mainImageClass = "post_photo_news_img";
    private static final String textClass = "post_text";
    private static final String withTimeClass = "post_time";




    @Override
    public News parsePage(String url) {
        News news;
        Document doc = loadDocument(url);
        String title = parseTitle(doc, titleClass);
        out.println(title);
        String mainImageURL = parseMainImage(doc, mainImageClass);
        out.println(mainImageURL);
        String text = parseText(doc, textClass);
        out.println(text);
        String fullDate = parseDate(doc, withTimeClass);
//        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMM uuuu"));
        out.println(fullDate);
        return null;
    }

    @Override
    public String parseDate(Document doc, String classWithTime) {
        return doc.getElementsByClass(classWithTime).text();
    }

    @Override
    public String parseText(Document doc, String classText) {
        return doc.getElementsByClass(classText).text();
    }

    private Document loadDocument(String url) {
        try {
            if (url.startsWith("/news")) return Jsoup.connect(baseUrl + url).get();
//            else return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Document("");
    }

    @Override
    public String parseMainImage(Document doc, String imageClass) {
        return doc.getElementsByClass(imageClass).attr("src");
    }

}
