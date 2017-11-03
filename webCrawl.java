import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
import java.util.regex.*;

import java.io.IOException;
import java.io.*;
/*creates a list of websites, with # backlinks, and page content associated to "url"
  ##url##backlinks##pagecontent
*/
public class webCrawl {
    public static void main(String[] args) throws IOException {
        Validate.isTrue(args.length == 1, "usage: supply url to fetch");
        String init_url = args[0];
        int myCount = 0;
        String myUrl = "";
        String body = "";
        Document doc = Jsoup.connect(init_url).get();
        Elements links = doc.select("a[href]");


        for(Element link : links){
          myUrl = link.attr("abs:href");
          if(myUrl != null && !myUrl.isEmpty()){
            Document linkDoc = Jsoup.connect(myUrl).get();
            myCount = (linkDoc.select("a[href]")).size();
            body = linkDoc.title(); //title only
        //    body =  linkDoc.text().toString(); //full page document

            print("%s##%d##%s",myUrl,myCount,body);
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
