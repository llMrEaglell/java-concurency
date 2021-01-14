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
    int getConnectionLimit();

    default Properties loadProperties(String fileName) throws IOException {
        InputStream resource = Main.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
        properties.load(resource);
        return properties;
    }

    String getFilter();
}
