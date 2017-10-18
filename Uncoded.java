import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.GenericOptionsParser;

public class Uncoded {
//constant instantiations
  public static final int rowA = 1000;
  public static final int colA = 1000;
  public static final int rowX = 1000;
  public static final int colX = 1;

  double [][] myA = new double[rowA][colA];
  double [][] myX = new double[rowX][colX];
  double [][] A1 = new double[rowA/2][colA];
  double [][] A2 = new double[rowX/2][colX];
  Random rand = new Random();

  /* Takes in myA and myX input and outputs '' for Reducer*/
  public static class Uncoded_map
   extends Mapper<Object, Text, Text, IntWritable> {

  }


// Receives output of Mapper and returns execution count
  public static class Uncoded_red
  extends Reducer<Text,IntWritable,Text,IntWritable> {

  }
//Generates row x col doubles from [0,1)
  public double[][] arrayMaker(int row, int col) {
      double[][] temp = new double[row][col];
      for (int i = 0; i < row; i++){
        for(int j = 0; j < col; j++){
         temp[i][j] = rand.nextDouble();
      //    System.out.println(temp[i][j]);
        }
      }
      return temp;
  }
  //Calls arrayMaker to set A and X matrices
  public void setArray(){
    myA = arrayMaker(rowA,colA);
    myX = arrayMaker(rowX,colX);
    //for debugging
    /*for (int i = 0; i < rowX; i++){
      for(int j = 0; j < colX; j++){
        System.out.println(myX[i][j]);
      }
    }*/
  }

  //splits myA into A1 and A2
  public void partition() {
    for (int  i = 0; i < rowA / 2; i++){
      for(int j = 0; j < colA; j++){
        A1[i][j] = myA[i][j];
      }
    }

    for (int  i = rowA/2 ; i < rowA; i++){
      for(int j = 0; j < colA; j++){
        A2[i][j] = myA[i][j];
      }
    }
  }


  //main
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    //String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length > 100) { //NOP
      System.err.println("Usage: wordcount <in> [<in>...] <out>");
      System.exit(2);
    }

    //instantiation arrays
    Uncoded myUncoded =new Uncoded();
    myUncoded.setArray();
    Uncoded.partition();
    long mytime = System.currentTimeMillis();
    System.out.println(mytime);

    Job job = Job.getInstance(conf);
    job.setJobName("Uncoded");
    job.setJarByClass(Uncoded.class);
    job.setMapperClass(Uncoded_map.class);
    job.setReducerClass(Uncoded_red.class);
  //  job.setOutputKeyClass(sumthin.class);
  //  job.setOutputValueClass(sumthin.class);

  }
}
