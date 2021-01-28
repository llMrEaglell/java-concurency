package com.newsagregator.site.properties.loader;

import com.newsagregator.NewsSites;
import com.newsagregator.PropertiesLoader;

import java.util.Properties;

public class KorrepsondentProperties implements NewsSiteProperties {
    private final String BLOCK_WITH_NEWS;
    private final String BASE_URL;
    private final String POST_ITEM_TITLE;
    private final String NEWS_CLASS;
    private final String ITEM_BIG_PHOTO_IMG;
    private final String POST_ITEM_TEXT;
    private final String WITH_TIME_CLASS;
    private final String POST_ITEM_TAGS_ITEM;
    private final String FILTER;
    private final int CONNECTION_LIMIT;

    public KorrepsondentProperties(String fileName){
        Properties cfg = PropertiesLoader.load(fileName);
        BASE_URL = cfg.getProperty("base.url");
        POST_ITEM_TITLE = cfg.getProperty("post.item.title");
        NEWS_CLASS = cfg.getProperty("news.class");
        ITEM_BIG_PHOTO_IMG = cfg.getProperty("item.big.photo.img");
        POST_ITEM_TEXT = cfg.getProperty("post.item.text");
        WITH_TIME_CLASS = cfg.getProperty("post.time.class");
        POST_ITEM_TAGS_ITEM = cfg.getProperty("post.item.tags");
        FILTER = cfg.getProperty("site.filter");
        CONNECTION_LIMIT = Integer.parseInt(cfg.getProperty("site.connection.limit"));
        BLOCK_WITH_NEWS = cfg.getProperty("block.with.news");
    }

    @Override
    public NewsSites getType() {
        return NewsSites.KORRESPONDENT;
    }

    @Override
    public String getBaseURL() {
        return BASE_URL;
    }

    @Override
    public String getPostItemTitle() {
        return POST_ITEM_TITLE;
    }

    @Override
    public String getNewsClass() {
        return NEWS_CLASS;
    }

    @Override
    public String getItemBigPhotoIMG() {
        return ITEM_BIG_PHOTO_IMG;
    }

    @Override
    public String getPostItemText() {
        return POST_ITEM_TEXT;
    }

    @Override
    public String getTimeClass() {
        return WITH_TIME_CLASS;
    }

    @Override
    public String getTagsItemClass() {
        return POST_ITEM_TAGS_ITEM;
    }

    @Override
    public String getBlockWithNews() {
        return BLOCK_WITH_NEWS;
    }

    @Override
    public int getConnectionLimit() {
        return CONNECTION_LIMIT;
    }

    @Override
    public String getFilter() {
        return FILTER;
    }
}
