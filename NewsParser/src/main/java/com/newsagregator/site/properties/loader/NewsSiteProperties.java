package com.newsagregator.site.properties.loader;

import com.newsagregator.NewsSites;

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

    NewsSites getType();
}
