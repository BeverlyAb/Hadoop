//import org.apache.hadoop.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
import java.util.regex.*;

import java.io.IOException;
import java.io.*;

/**
 * Example program to list links from a URL.
 */
public class ListLinks {
    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 2, "usage: supply url to fetch");
        String url = args[0];
        String wordSearch = args[1];

        wordSearch = "\\b"+wordSearch+"\\b";
        print("word %s.....",wordSearch);
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        print("\nLinks: (%d)", links.size());
        String linkName = "";
        int i = 0;

        for (Element link : links) {
          linkName = link.attr("abs:href");

            if(linkName != null && !linkName.isEmpty()){
            Document linkDoc = Jsoup.connect(linkName).get();
            String body  = linkDoc.text();

            Pattern pattern = Pattern.compile(wordSearch);
            Matcher matcher = pattern.matcher(body);

            if(matcher.find()) {
              print("i = %d",i++);
              print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
            }
        }
      }
    }
    

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
