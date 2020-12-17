package newsAgregator.parsers;

import newsAgregator.dateParser.DateParser;
import newsAgregator.news.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class UkrainePravdaNewsPageParser implements Parser {

    private static final String baseUrl = "https://www.pravda.com.ua/";
    private static final String titleClass = "post_title";
    private static final String mainImageClass = "post_photo_news_img";
    private static final String textClass = "post_text";
    private static final String withTimeClass = "post_time";
    private static final String tagsClass = "post_tags_item";


    @Override
    public News parsePage(String url) {
        Document doc = loadDocument(url);
        String title = parseTitle(doc, titleClass);
        String mainImageURL = parseMainImage(doc, mainImageClass);
        String text = parseText(doc, textClass);
        LocalDate localDate = DateParser.parse(parseDate(doc, withTimeClass).split(",")[1]);
        List<String> tags = parseTags(doc, tagsClass);
        return new News(title, text, localDate, mainImageURL, tags);
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
            return Jsoup.connect(baseUrl + url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Document("");
    }

    @Override
    public List<String> parseTags(Document doc, String tagsClass) {
        return doc.getElementsByClass(tagsClass).stream()
                .map(element -> element.getElementsByTag("a").text())
                .collect(Collectors.toList());
    }

    @Override
    public String parseMainImage(Document doc, String imageClass) {
        return doc.getElementsByClass(imageClass).attr("src");
    }

}
