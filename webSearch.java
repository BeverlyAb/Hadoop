


import java.util.*;
import java.util.regex.*;

import java.io.IOException;
import java.io.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import org.jsoup.select.Elements;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
//import org.jsoup.helper.Validate;

public class webSearch {
  public static String myWord = new String();
  /*returns a list of urls that are associated to myLinks and contain  myWord */
  public static class urlMapper extends Mapper<LongWritable, Text, Text, BooleanWritable>
   {
      private BooleanWritable hasWord = new BooleanWritable();
      private Text urlMapOut = new Text();

      public void map(LongWritable key, Text urlIn, Context context) throws IOException, InterruptedException
      {
        String myUrl = urlIn.toString();
        Document doc = Jsoup.connect(myUrl).get();
        Elements myLinks = doc.select("a[href]");

        for (Element mylink : myLinks) {
          String linkName = mylink.attr("abs:href");

          if(linkName != null && !linkName.isEmpty()){
              Document linkDoc = Jsoup.connect(linkName).get();
              String body = linkDoc.text();

              Pattern pattern = Pattern.compile(myWord);
              Matcher matcher = pattern.matcher(body);
              urlMapOut = new Text(linkName);
              hasWord = new BooleanWritable(matcher.find());
              context.write(urlMapOut, hasWord);
            }
          }
      }
   }
   /* returns the number of links backlinked to myUrl
     Ex)
     (Source) -> (Target)
     a -> b -> c
     Count: a = 2, b = 1, c = 0
   */
   public static class rankReducer extends Reducer<Text,BooleanWritable,Text, IntWritable>
   {
     private IntWritable myCount = new IntWritable();

     public void reduce(Text urlMapOut, BooleanWritable hasWord, Context context) throws IOException, InterruptedException
     {
       Document linkDoc = Jsoup.connect(urlMapOut.toString()).get();
       myCount = new IntWritable((linkDoc.select("a[href]")).size());
       context.write(urlMapOut, myCount);
     }
  }

public static void main(String args[])throws Exception
  {
  //  Validate.isTrue(args.length == 3, "usage: supply url to fetch");
    myWord = args[2];
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "web search");

    job.setJarByClass(webSearch.class);
    job.setMapperClass(urlMapper.class);
  //  job.setCombinerClass(rankReducer.class);
    job.setReducerClass(rankReducer.class);

    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(BooleanWritable.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
/*
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

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    } */
}
