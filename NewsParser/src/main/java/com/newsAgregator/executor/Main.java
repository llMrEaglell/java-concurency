package com;

import newsAgregator.crawler.Crawler;
import newsAgregator.crawler.webcrawlers.KorrespondentCrawler;
import newsAgregator.crawler.webcrawlers.UkrainePravdaCrawler;
import newsAgregator.news.News;
import newsAgregator.parsers.KorrespondentNewsPageParser;
import newsAgregator.parsers.Parser;
import newsAgregator.parsers.UkrainePravdaNewsPageParser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
//        Crawler ukraineTrueCrawler = new UkrainePravdaCrawler();
//        List<String> urlPages =  ukraineTrueCrawler.getPages(100);
//        System.out.println(urlPages);
//        Parser newsParser = new UkrainePravdaNewsPageParser();
//        for (String urlPage : urlPages) {
//            News news =  newsParser.parsePage(urlPage);
//            System.out.println(news);
//        }

        Crawler korrepsondentCrawler = new KorrespondentCrawler();
        List<String> pages = korrepsondentCrawler.getPages(200);
        System.out.println(pages);
//        Parser korrespondentParser = new KorrespondentNewsPageParser();
//        for (String page : pages) {
//            News news = korrespondentParser.parsePage(page);
//            System.out.println(news);
//        }



    }
}
