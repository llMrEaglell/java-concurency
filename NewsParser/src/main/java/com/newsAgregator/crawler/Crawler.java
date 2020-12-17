package newsAgregator.crawler;

import org.jsoup.nodes.Document;

import java.util.List;
import java.util.stream.Collectors;

public interface Crawler {
    List<String> getPages(int count);

    default List<String> uploadLinksForNewsOnPage(Document pageWithAllNews, String classNameWithNews) {
        return pageWithAllNews.body()
                .getElementsByClass(classNameWithNews).stream()
                .map(element -> element.getElementsByTag("a").attr("href"))
                .collect(Collectors.toList());
    }
    default void addToList(List<String> source, List<String> urls, int maxSize) {
        urls.stream().takeWhile(url -> source.size() != maxSize).forEach(source::add);
    }
}
