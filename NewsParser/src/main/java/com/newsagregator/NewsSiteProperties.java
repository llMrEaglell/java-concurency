package com.newsagregator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    default Properties loadProperties(String fileName) {
        InputStream resource = Main.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
        try {
            properties.load(resource);
        } catch (IOException e) {
            System.err.println("Can't load properties:" + fileName);
        }

        return properties;
    }

    String getFilter();
}
