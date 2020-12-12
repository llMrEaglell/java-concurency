package parsers;

import news.News;
import org.jsoup.nodes.Document;

import java.util.List;

public interface Parser {
    News parsePage(String url);

    default String parseTitle(Document document, String titleClass){
        return document.body()
                .getElementsByClass(titleClass).text();
    }

    String parseDate(Document doc, String classWithTime);

    String parseText(Document doc, String classText);

    String parseMainImage(Document doc, String imageClass);

    List<String> parseTags(Document doc, String tagsClass);
}
