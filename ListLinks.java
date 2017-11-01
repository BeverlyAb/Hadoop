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
        long start = System.nanoTime();

        Validate.isTrue(args.length == 2, "usage: supply url to fetch");
        String url = args[0];
        String wordSearch = args[1];

        wordSearch = "\\b"+wordSearch+"\\b";
        print("word %s.....",wordSearch);
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        print("\nLinks: (%d)", links.size());
        List<String> myUrls = finder(links,wordSearch);
        for(Element link :links) {
          int rank = backCounter(link);
          print("ranking for %s is %d",link.attr("abs:href"),rank);
        }

        for(String single_url : myUrls){
          print(single_url);
        }
        long dif = (System.nanoTime() - start);
        print("my execution time =  " + Objects.toString(dif));
    }
  /*returns a list of urls that are associated to myLinks and contain  myWord" */
   private static List<String> finder (Elements myLinks, String myWord) throws IOException {
      String linkName = "";
      List<String> myurls = new ArrayList<String>();

      for (Element mylink : myLinks) {
        linkName = mylink.attr("abs:href");

          if(linkName != null && !linkName.isEmpty()){
            Document linkDoc = Jsoup.connect(linkName).get();
            String body = linkDoc.text();

            Pattern pattern = Pattern.compile(myWord);
            Matcher matcher = pattern.matcher(body);

            if(matcher.find()) {
              myurls.add(mylink.attr("abs:href"));
            }
          }
      }
      return myurls;
    } //finder

    /* returns the number of links backlinked to myUrl
      Ex)
      (Source) -> (Target)
      a -> b -> c
      Count: a = 2, b = 1, c = 0
    */
    public static int backCounter(Element myUrl) throws IOException {
        int myCount = 0;
        String linkName = myUrl.attr("abs:href");
        if(linkName != null && !linkName.isEmpty()){
          Document linkDoc = Jsoup.connect(linkName).get();
          myCount = (linkDoc.select("a[href]")).size();
        }
        return myCount;
    }//backCounter

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
