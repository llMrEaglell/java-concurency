package com.newsagregator.parsers;

import com.newsagregator.dateParser.DateParser;
import com.newsagregator.news.News;
import org.jsoup.nodes.Document;

import java.time.LocalDate;
import java.util.List;

public class StranaNewsPageParser extends BasePageParser implements Parser {

    public StranaNewsPageParser(String titleClass, String mainImageClass, String textClass, String withTimeClass, String tagsClass) {
        super(titleClass, mainImageClass, textClass, withTimeClass, tagsClass);
    }

    @Override
    public News parsePage(Document doc) {
        String title = parseTitle(doc, titleClass);
        String mainImageURL = parseMainImage(doc, mainImageClass);
        String text = parseText(doc, textClass);
        String dateRow = parseDate(doc, withTimeClass);
        LocalDate localDate = DateParser.parseDate(dateRow);
        List<String> tags = parseTags(doc, tagsClass);

        return new News(title, text, localDate, mainImageURL, tags, "strana.ua");
    }

    @Override
    public String parseText(Document doc, String classText) {
        return doc.getElementById(classText).text();
    }

    @Override
    public String parseMainImage(Document doc, String imageClass) {
        try {
            return doc.baseUri().substring(0, 17) +
                    doc.body().getElementsByClass("article-image").get(0)
                            .childNodes().get(1).attr("src");
        } catch (IndexOutOfBoundsException e) {
            return "";
        }

    }
}


