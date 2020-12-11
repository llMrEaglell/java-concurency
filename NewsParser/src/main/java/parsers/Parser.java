package parsers;

import news.News;

public interface Parser {
    News parsePage(String url);
}
