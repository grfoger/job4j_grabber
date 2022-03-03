package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.model.Post;

import java.io.IOException;
import java.time.LocalDateTime;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        System.out.println(getPost("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t"));
        /**
        String url = "https://www.sql.ru/forum/job-offers/";
        int pages  = 5;
        for (int i = 1; i <= pages; i++) {
            Document doc = Jsoup.connect(url + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                Element parent = td.parent();
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(parent.child(5).text());
            }
        }
        */
    }

    public static Post getPost(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Element body = doc.selectFirst(".msgTable").child(0);
        Element title = body.child(0);
        Element description = body.child(1).child(1);
        Element date_create = body.child(2).child(0);
        String date = date_create.text().split("\\[")[0];
        DateTimeParser parser = new SqlRuDateTimeParser();
        LocalDateTime time = parser.parse(date.substring(0,date.length() - 1));
        return new Post(title.text(), url, description.text(), time);
    }
}