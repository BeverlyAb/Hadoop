import java.lang.Math.*; // pow
import java.util.*;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DNA_MR {
  public static int [] myInt;
  public static String word = "";
  public static int l = 5; //17 overall seq = 33
    //encodes
  public static class encodeMapper extends Mapper<Object, Text, Text, Text>
   {
     private String index = "";
     private String addr = "";
     private String encodeOut = "";

     public void map(Object key_index, Text database, Context context) throws IOException, InterruptedException
     {
       String [] index_addr = database.toString().split("##");
       index = index_addr[0];
       addr = index_addr[1];
       for (int i = 0; i < word.length(); i ++) {
         encodeOut = addr + (encode(myInt[i], l ,addr)) + addr;
         context.write(new Text(word.charAt(i) + "\t" + addr),new Text(encodeOut));
       }
    }
  }

  //only returns encoded sequences with proper GC content
  public static class gcReducer extends Reducer<Text,Text,Text, Text>
  {
    public void reduce(Text key_addr, Iterable<Text> encoded_list, Context context) throws IOException, InterruptedException
    {
      for(Text encodeOut : encoded_list){
        if(addrChecker(encodeOut, key_addr) && GCchecker(encodeOut)) {
          context.write(key_addr, encodeOut);
        }
      }
    }
 }

 public static void main(String args[])throws Exception
   {
     word = args[2];
     myInt = asciiToDec(word);

     Configuration conf = new Configuration();
     Job job = Job.getInstance(conf, "encoder");

     job.setJarByClass(DNA_MR.class);
     job.setMapperClass(encodeMapper.class);
     job.setReducerClass(gcReducer.class);

     job.setMapOutputKeyClass(Text.class);
     job.setMapOutputValueClass(Text.class);

     job.setOutputKeyClass(Text.class);
     job.setOutputValueClass(Text.class);

     FileInputFormat.addInputPath(job, new Path(args[0]));
     FileOutputFormat.setOutputPath(job, new Path(args[1]));

     System.exit(job.waitForCompletion(true) ? 0 : 1);
   }
// Algorithm 1 of report; returns encoded string of DNA
  public static String encode(double myX, int l, String addr) {
    int n = addr.length();
    String encodeOut = "";
    int t;

    if(l >= n) {
      double y = myX;
      t = 1;
      while(y >= (setAi(addr,t-1).length()) * sGen(addr,l)[l-t -1]){ // starts[0]
        y = y - (setAi(addr,t-1).length()) * sGen(addr,l)[l-t - 1];
        t++;
      }
      double c = y / (sGen(addr,l)[l-t -1]);
      double d = y % (sGen(addr,l)[l-t - 1]);

      if(t -1 > 0)
        encodeOut = encodeOut + addr.charAt(t-2);

      encodeOut = encodeOut + setAi(addr,t-1).charAt((int)c);
      encodeOut = encodeOut + encode((int)d, l-t, addr);
      return encodeOut;
    }
    else
      return ternary(myX, l);
  } // encode

  //converts dec to DNA representation
  public static String decToDNA(int in) {
    if(in == 0)
      return "A";
    else if (in == 1)
      return "T";
    else if (in == 2)
      return "C";
    else //invalid
      return "X";
  }

  // returns ternary rep. given base l
  public static String ternary(double myX, int l) {
    String ternOut = "";
    double temp = myX;
    int coeff = 0;

    for(int i = l - 1; i >= 0; i--) {
      if(temp >= Math.pow(3,i)){
        while(temp >= Math.pow(3,i)) {
          temp = temp - Math.pow(3,i);
          coeff++;
        }
      }
      ternOut = ternOut + decToDNA(coeff);
      coeff = 0;
    }
    return ternOut;
  } // ternary

  //returns set based on ai,
  public static String setAi(String in, int index){
    char ai = in.charAt(index);

    String setOut = "";
    for(int i = 0; i < in.length(); i++) {
      //exclude G due to generality
      if(ai != in.charAt(i) && 'G'!= in.charAt(i) )
        setOut = setOut + in.charAt(i);
    }
    return setOut;
  }

  //generates Sn,l based on Fig. 1 from report
  public static double [] sGen(String addr, int l){
    double [] myS = new double[l-1];
    double sum = 0;
    String set = "";

    for(int i = 1; i < l; i++){
        if(i < addr.length()) {
          myS[i-1] = Math.pow(3,i);
        }
        else {
          int j = 1;
          while(j < addr.length()) {
            set = setAi(addr, j-1);
            sum = sum + set.length() * myS[i-1-j];
            j++;
          }
          myS[i-1] = sum;
          sum = 0;
        }
    }
    return myS;
  }

  //checks constraint 1:
  //checks if the info. part of DNA contains generated address
  // format: addr [5] + info [20] + addr [5]
  public static boolean addrChecker(Text in, Text addr){
    int addrLen = addr.toString().length() - 2;
    int inLen = in.toString().length();
    String window = "";

    int endOfInfo = inLen - 2 * addrLen;
    for (int i = addrLen + 2; i < endOfInfo + addrLen; i++) {//no tab && letter
      window = in.toString().substring(i,i +addrLen);
      if(window.equals(addr.toString()))
        return false;
    }
    return true;
  } //addrChecker

  //checks constraint 2: GC content
  public static boolean GCchecker(Text in){
      double count = 0;
      for (int i = 0; i < in.toString().length(); i++) {
        if(in.toString().charAt(i) == 'C' || in.toString().charAt(i) == 'G')
          count++;
      }
      return  ((count / in.toString().length()) >= 0.48 &&
              (count / in.toString().length()) <= 0.52 );
  } //GCchecker

  //converts ascii to decimal
  public static int[] asciiToDec(String in){
    int [] dec = new int[in.length()];

    for(int i = 0; i < in.length(); i++){
      dec[i] = in.charAt(i) - '0' + 48;
    }
    return dec;
  }
}
