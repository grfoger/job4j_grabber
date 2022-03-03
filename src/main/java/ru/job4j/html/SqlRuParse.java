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
    public static void main(String[] args) throws Exception {
        String url = "https://www.sql.ru/forum/job-offers/";
        Parse parser = new SqlRuParse();
        System.out.println(parser.list(url).toString());
    }

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>();
        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            posts.add(detail(td.child(0).attr("href")));
        }
        return posts;
    }

    @Override
    public Post detail(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Element body = doc.selectFirst(".msgTable").child(0);
        Element title = body.child(0);
        Element description = body.child(1).child(1);
        Element dateCreate = body.child(2).child(0);
        String date = dateCreate.text().split("\\[")[0];
        DateTimeParser parser = new SqlRuDateTimeParser();
        LocalDateTime time = parser.parse(date.substring(0, date.length() - 1));
        return new Post(title.text(), link, description.text(), time);
    }
}