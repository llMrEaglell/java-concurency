package com.newsAgregator.executor;


import com.newsAgregator.AgregatorStrategy;
import com.newsAgregator.KorrespondentAgregatorStrategy;

public class Main {
    public static void main(String[] args) {

        AgregatorStrategy strategy = new KorrespondentAgregatorStrategy();

        strategy.getNews(1000);


    }
}
