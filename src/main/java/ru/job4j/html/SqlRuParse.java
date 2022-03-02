package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
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
    }
}