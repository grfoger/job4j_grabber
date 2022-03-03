package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.grabber.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) throws Exception {
        String url = "https://www.sql.ru/forum/job-offers/";
        Parse parser = new SqlRuParse(new SqlRuDateTimeParser());
        System.out.println(parser.list(url).toString());
    }

    @Override
    public List<Post> list(String link) {
        List<Post> posts = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Post post = detail(td.child(0).attr("href"));
                boolean containsJava = post.getTitle().toLowerCase().contains("java");
                boolean containsJS = post.getTitle().toLowerCase().contains("javascript");
                boolean isOnlyJava = containsJava && !containsJS;
                if (!isOnlyJava) {
                    continue;
                }
                posts.add(post);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        Post post = null;
        try {
            Document doc = Jsoup.connect(link).get();
            Element body = doc.selectFirst(".msgTable").child(0);
            Element title = body.child(0);
            Element description = body.child(1).child(1);
            Element dateCreate = body.child(2).child(0);
            String date = dateCreate.text().split("\\[")[0];
            LocalDateTime time = dateTimeParser.parse(date.substring(0, date.length() - 1));
            post = new Post(title.text(), link, description.text(), time);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }
}