package com.newsagregator.parsers;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BasePageParser implements Parser {
    protected final String titleClass;
    protected final String mainImageClass;
    protected final String textClass;
    protected final String withTimeClass;
    protected final String tagsClass;

    protected BasePageParser(String titleClass, String mainImageClass, String textClass, String withTimeClass, String tagsClass) {
        this.titleClass = titleClass;
        this.mainImageClass = mainImageClass;
        this.textClass = textClass;
        this.withTimeClass = withTimeClass;
        this.tagsClass = tagsClass;
    }

    @Override
    public String parseTitle(Document document, String titleClass){
        return document.body()
                .getElementsByClass(titleClass).text();
    }

    @Override
    public String parseDate(Document doc, String classWithTime) {
        return doc.getElementsByClass(classWithTime).text();
    }

    @Override
    public List<String> parseTags(Document doc, String tagsClass) {
        return doc.getElementsByClass(tagsClass).stream()
                .map(element -> element.getElementsByTag("a").text())
                .collect(Collectors.toList());
    }

}
