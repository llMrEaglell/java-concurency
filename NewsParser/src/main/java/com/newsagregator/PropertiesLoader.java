package com.newsagregator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.System.err;

public class PropertiesLoader {
    private PropertiesLoader(){
        throw new IllegalStateException("Utility class for load properties from file");
    }
    public static Properties load(String fileName){
        InputStream resource = Main.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();

        try {
            properties.load(resource);
        } catch (IOException e) {
            err.println("Can't load properties:" + fileName);
        }

        return properties;
    }
}
