package newsAgregator.news;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class News {

    private String title;
    private String text;
    private String mainImageURL;
    private String source;
    private LocalDate datePublication;
    private List<String> tags;

    public News(String title, String text, LocalDate datePublication) {
        this.title = title;
        this.text = text;
        this.datePublication = datePublication;
        this.mainImageURL = "";
        this.source = "own";
        this.tags = new ArrayList<>();
    }

    public News(String title, String text, LocalDate datePublication,String mainImageURL) {
        this(title,text,datePublication);
        this.mainImageURL = mainImageURL;
    }

    public News(String title, String text, LocalDate datePublication,String mainImageURL,List<String> tags) {
        this(title,text,datePublication,mainImageURL);
        this.tags = tags;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMainImageURL(String mainImageURL) {
        this.mainImageURL = mainImageURL;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getMainImageURL() {
        return mainImageURL;
    }

    public String getSource() {
        return source;
    }

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Objects.equals(title, news.title) && Objects.equals(text, news.text) && Objects.equals(mainImageURL, news.mainImageURL) && Objects.equals(source, news.source) && Objects.equals(datePublication, news.datePublication) && Objects.equals(tags, news.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, text, mainImageURL, source, datePublication, tags);
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", mainImageURL='" + mainImageURL + '\'' +
                ", source='" + source + '\'' +
                ", datePublication=" + datePublication +
                ", tags=" + tags +
                '}';
    }
}
