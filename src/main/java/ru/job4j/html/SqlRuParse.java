package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.model.Post;

import java.io.IOException;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {

        System.out.println(getPost("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t").toString());

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
        Element title = doc.selectFirst(".messageHeader");
        Element description = title.parent().parent().child(1).child(1);

        Post post = new Post();
        post.setTitle(title.text());
        post.setDescription(description.text());
        post.setLink(url);
        return post;
    }


}