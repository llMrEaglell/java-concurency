package com.newsAgregator.parsers;

import com.newsAgregator.news.News;
import com.newsAgregator.dateParser.DateParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class KorrespondentNewsPageParser implements Parser {
    private final String titleClass;
    private final String mainImageClass;
    private final String textClass;
    private final String withTimeClass;
    private final String tagsClass;

    public KorrespondentNewsPageParser(String titleClass, String mainImageClass, String textClass, String withTimeClass, String tagsClass) {
        this.titleClass = titleClass;
        this.mainImageClass = mainImageClass;
        this.textClass = textClass;
        this.withTimeClass = withTimeClass;
        this.tagsClass = tagsClass;
    }

    @Override
    public News parsePage(String url) {
        Document doc = loadDocument(url);
        String title = parseTitle(doc, titleClass);
        String mainImageURL = parseMainImage(doc, mainImageClass);
        String text = parseText(doc, textClass);
        String date = parseDate(doc, withTimeClass).split(",")[1];
        LocalDate localDate = DateParser.parse(date);
        List<String> tags = parseTags(doc, tagsClass);
        return new News(title, text, localDate, mainImageURL, tags);
    }


    private Document loadDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Document("");
    }

    @Override
    public String parseDate(Document doc, String classWithTime) {
        return doc.getElementsByClass(classWithTime).text();
    }

    @Override
    public String parseText(Document doc, String classText) {
        return doc.getElementsByClass(classText).text();
    }

    @Override
    public String parseMainImage(Document doc, String imageClass) {
        return doc.getElementsByClass(imageClass).attr("src");
    }

    @Override
    public List<String> parseTags(Document doc, String tagsClass) {
        return doc.getElementsByClass(tagsClass).stream()
                .map(element -> element.getElementsByTag("a").text())
                .collect(Collectors.toList());
    }
}
