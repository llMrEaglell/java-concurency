package com.newsagregator.parsers;

import com.newsagregator.news.News;
import org.jsoup.nodes.Document;

import java.util.List;

public interface Parser {
    News parsePage(Document doc);

    String parseTitle(Document document, String titleClass);

    String parseDate(Document doc, String classWithTime);

    String parseText(Document doc, String classText);

    String parseMainImage(Document doc, String imageClass);

    List<String> parseTags(Document doc, String tagsClass);
}
