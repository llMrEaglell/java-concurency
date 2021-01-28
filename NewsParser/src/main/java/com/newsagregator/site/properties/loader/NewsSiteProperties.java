package com.newsagregator.site.properties.loader;

public interface NewsSiteProperties {
    String getBaseURL();

    String getPostItemTitle();

    String getNewsClass();

    String getItemBigPhotoIMG();

    String getPostItemText();

    String getTimeClass();

    String getTagsItemClass();

    String getBlockWithNews();

    int getConnectionLimit();

    String getFilter();
}
