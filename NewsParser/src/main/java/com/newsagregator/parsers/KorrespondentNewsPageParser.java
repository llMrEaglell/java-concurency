package com.newsagregator.parsers;

import com.newsagregator.dateParser.DateParser;
import com.newsagregator.news.News;
import org.jsoup.nodes.Document;

import java.time.LocalDate;
import java.util.List;

import static java.lang.System.err;

public class KorrespondentNewsPageParser extends BasePageParser implements Parser {

    public KorrespondentNewsPageParser(String titleClass, String mainImageClass, String textClass, String withTimeClass, String tagsClass) {
        super(titleClass, mainImageClass, textClass, withTimeClass, tagsClass);
    }

    @Override
    public News parsePage(Document doc) {
        String title = parseTitle(doc, titleClass);
        String mainImageURL = parseMainImage(doc, mainImageClass);
        String text = parseText(doc, textClass);
        String dateRow = parseDate(doc, withTimeClass);
        String date = " Сегодня";

        try {
            date = dateRow.split(",")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            err.println(dateRow);
            err.println(doc.location());
        }

        LocalDate localDate = DateParser.parse(date);
        List<String> tags = parseTags(doc, tagsClass);

        return new News(title, text, localDate, mainImageURL, tags, "korrespondent.net");
    }

    @Override
    public String parseText(Document doc, String classText) {
        return doc.getElementsByClass(classText).text();
    }

    @Override
    public String parseMainImage(Document doc, String imageClass) {
        return doc.getElementsByClass(imageClass).attr("src");
    }

}
