package com.newsagregator;

import com.newsagregator.news.News;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

public class NewsRepository {

    private static final String SQL_INSERT_ALL_NEWS;

    static {
        SQL_INSERT_ALL_NEWS = "INSERT INTO NEWS (title, text, image,source,date,tags) VALUES (?,?,?,?,?,?)";
    }

    private final DataSource dataSource;

    public NewsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void saveNews(Set<News> allNews) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ALL_NEWS)) {
            for (News news : allNews) {
                    ps.setString(1, news.getTitle());
                    ps.setString(2, news.getText());
                    ps.setString(3, news.getMainImageURL());
                    ps.setString(4, news.getSource());
                    ps.setString(5, news.getDatePublication().toString());
                    ps.setString(6, news.getTags().toString());
                    ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
