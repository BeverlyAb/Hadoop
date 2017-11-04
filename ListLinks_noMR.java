// No MapReduce
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;
import java.util.regex.*;

import java.io.IOException;
import java.io.*;

public class ListLinks {
  public static Map<Integer,String> urlBack = new TreeMap<Integer,String>();

  public static void main(String[] args) throws IOException {
    long start = System.nanoTime();

    Validate.isTrue(args.length == 2, "usage: supply url to fetch");
    String dataIn = args[0];
    String myWord = args[1];

  	FileInputStream fis = new FileInputStream(dataIn);

  	//Construct BufferedReader from InputStreamReader
  	BufferedReader br = new BufferedReader(new InputStreamReader(fis));
    String url ="";
    String back ="";
    String pgBlock  ="";
  	String line = null;

    int i = 0;
    int j = 0;
    //parse and look for words
  	while ((line = br.readLine()) != null) {
      String [] url_back_pg =  line.split("##");
      url = url_back_pg[0];
      back = url_back_pg[1];
      pgBlock = url_back_pg[2];

    //  print("line %s",line);
//      print("url %s back %s pgBlock %s",url,back,pgBlock);

      String [] pageWord = pgBlock.split(" ");
    //  while(!(myWord.toLowerCase()).equals(pageWord[i].toLowerCase()) && (pageWord.length -1> i)){
  //      System.out.print(pageWord[i].toLowerCase());
  //        System.out.print(": i = ");System.out.println(i);
  //        System.out.print("length = ");System.out.println(pageWord.length);
  //      i++;
  //    }

      //contains word
      if((myWord.toLowerCase()).equals(pageWord[i])){ //add url and back to list
        urlBack.put(Integer.parseInt(back),url);
      }
  	}
  	br.close();

    for(Integer key : urlBack.keySet()) {
      System.out.println(urlBack.get(key));
    }
    long dif = (System.nanoTime() - start);
    System.out.println("my execution time =  " + Objects.toString(dif));
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
