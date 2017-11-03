


import java.util.*;
import java.util.regex.*;

import java.io.IOException;
import java.io.*;
import java.nio.ByteBuffer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class webSearch {
  public static String myWord = new String();
  /*returns a list of urls and backlink that are associated to myLinks and contain  myWord */
  public static class urlMapper extends Mapper<Object, Text, Text, IntWritable>
   {
      private String url ="";
      private String back ="";
      private String pgBlock  ="";
      public void map(Object key_url, Text database, Context context) throws IOException, InterruptedException
      {
        String[] url_back_pg = database.toString().split("##");
        url = url_back_pg[0];
        back = url_back_pg[1];
        pgBlock = url_back_pg[2];

        int  i = 0;
        String [] pageWord = pgBlock.split(" ");

        System.out.print("myWord = ");System.out.println(myWord);
        while(!myWord.equals(pageWord[i]) && (pageWord.length - 1> i)){
          System.out.print(pageWord[i]);
          System.out.print(": i = ");System.out.println(i);
          System.out.print("length = ");System.out.println(pageWord.length);
          i++;
        }

        if(myWord.equals(pageWord[i])){
          context.write(new Text(url), new IntWritable(Integer.parseInt(back)));
        }
      }
    }

   public static class IntComparator extends WritableComparator {

       public IntComparator() {
           super(IntWritable.class);
       }

       @Override
       public int compare(byte[] b1, int s1, int l1,
               byte[] b2, int s2, int l2) {

           Integer v1 = ByteBuffer.wrap(b1, s1, l1).getInt();
           Integer v2 = ByteBuffer.wrap(b2, s2, l2).getInt();

           return v1.compareTo(v2) * (-1);
       }
   }

   public static class rankReducer extends Reducer<Text,IntWritable,Text, IntWritable>
   {
     private IntWritable myCount = new IntWritable();

     public void reduce(Text key_url, Iterable<IntWritable> back_list, Context context) throws IOException, InterruptedException
     {
       for(IntWritable back : back_list){
         context.write(key_url,back);
       }
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
    job.setSortComparatorClass(IntComparator.class);
    job.setReducerClass(rankReducer.class);

    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
