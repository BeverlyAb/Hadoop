import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;

public class MDS {
//constant instantiations
  public static final int rowA = 1000;
  public static final int colA = 1000;
  public static final int rowX = 1000;
  public static final int colX = 1;

  double [][] myA = new double[rowA][colA];
  double [][] myX = new double[rowX][colX];
  Random rand = new Random();

  /* Takes in myA and myX input and outputs '' for Reducer*/
//  public void MDS_map(double A[][]){

//  }


/* Receives output of Mapper and returns execution count
//  public void MDS_red(double A[][]){

//  } */
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
    for (int i = 0; i < rowX; i++){
      for(int j = 0; j < colX; j++){
        System.out.println(myX[i][j]);
      }
    }
  }
  //main
  public static void main(String[] args) {
    //instantiation arrays
    MDS myMDS =new MDS();
    myMDS.setArray();
/*
    Configuration conf = getConf();
    Job job = Job.getInstance(conf);
    job.setJobName("MDS");
    job.setJarByClass(MDS.class);
    job.setMapperClass(MDS_map.class);
    job.setReducerClass(MDS_red.class);
    job.setOutputKeyClass()
*/  }
}
