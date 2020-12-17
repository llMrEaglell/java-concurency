package com.newsAgregator.executor;


import com.newsAgregator.crawler.Crawler;
import com.newsAgregator.crawler.webcrawlers.KorrespondentCrawler;

import java.util.List;
import java.util.Set;

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
        Set<String> pages = korrepsondentCrawler.getPages(1000);
        System.out.println(pages);
//        Parser korrespondentParser = new KorrespondentNewsPageParser();
//        for (String page : pages) {
//            News news = korrespondentParser.parsePage(page);
//            System.out.println(news);
//        }



    }
}
