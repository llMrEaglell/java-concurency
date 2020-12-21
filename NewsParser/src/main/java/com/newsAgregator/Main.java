package com.newsAgregator;


import com.newsAgregator.strategy.AgregatorStrategy;
import com.newsAgregator.strategy.KorrespondentAgregatorStrategy;

public class Main {
    public static void main(String[] args) {

        AgregatorStrategy strategy = new KorrespondentAgregatorStrategy();
        strategy.getNews(10000);

    }
}
