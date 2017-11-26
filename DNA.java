import java.lang.Math.*; // pow
import java.util.*;

public class DNA {
  static String [] x = {"ATCG", "ATAG", "CTAG", "CTCG", "ATTG", "CTTG"};
  static String [] y = {"ACCG", "AAAG", "CAAG", "CCCG", "ATTT", "CTTT"};
/*
static String [] x = {"ATCGATCG", "ATCGATAG", "ATCGCTAG", "ATCGCTCG", "ATCGATTG", "ATCGCTTG"};
static String [] y = {"GATAACCG", "GATAAAAG", "GATACAAG", "GATACCCG", "GATAATTT", "GATACTTT"};
*/

  public static void main(String [] args) {
      //adddrChecker
      /*System.out.println(args[0]);
      System.out.println(args[1]);
      if (addrChecker(args[0],args[1]))
        System.out.println("good");
      else
        System.out.println("bad"); */

      //combiner
      String allAddr [] = combine(x,y);
/*    for(int i = 0; i < allAddr.length; i++) {
        System.out.print(i + ": ");
        System.out.println(allAddr[i]);
      } */


      //setAi
    //  System.out.println(allAddr[0]);
    allAddr[0] = "AGCTG";
      for(int i = 0; i < allAddr[0].length(); i++) {
        System.out.print(i + ": ");
        System.out.println(setAi(allAddr[0], i));
      }

      //sGen
  /*   String addr = "AGCTG";
    //String addr = "ACCGATCG";
      double [] allS = sGen(addr,100);
      for(int i = 0; i < allS.length; i++) {
        System.out.print(i + ": ");
        System.out.println(allS[i]);
      } */

      //decToDNA
  //    System.out.print(decToDNA(1));

      //ternary
    //  System.out.println(ternary(16,4));

      //encoder
    //  for(int i = 0; i < allAddr.length; i++){
//System.out.println(encode(550,8,allAddr[i]));
        System.out.println(encode(550,100,"AGCTG"));
//          encode(550,50,allAddr[0]);
//}

  }

  //converts dec to DNA representation
  public static String decToDNA(int in) {
    if(in == 0)
      return "A";
    else if (in == 1)
      return "T";
    else if (in == 2)
      return "C";
    else if (in == 3)
      return "G";
    else //invalid
      return "X";
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
      System.out.println(d);
      if(t -1 > 0) {
        encodeOut = encodeOut + addr.charAt(t-2);
      //  System.out.println(addr.charAt(t-2));
      }
    //  System.out.println(setAi(addr,t-1).charAt((int)c));
      encodeOut = encodeOut + setAi(addr,t-1).charAt((int)c);
    //  System.out.println((int)d);
      return encodeOut + encode((int)d, l-t, addr);
    }
    else
      return ternary(myX, l);
  } // encode

  // returns ternary rep. given base l
  public static String ternary(double myX, int l) {
    String ternOut = "";
    double temp = myX;
    int coeff = 0;

    for(int i = l -1; i >= 0; i--) {
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

  //returns set based on ai, ***might have to sort to ascending order***
  public static String setAi(String in, int index){
    char ai = in.charAt(index);

    String setOut = "";
    for(int i = 0; i < in.length(); i++) {
      //exclude G due to generality
      if(ai != in.charAt(i) && 'G'!= in.charAt(i) )
        setOut = setOut + in.charAt(i);
    }
    int [] temp = new int [setOut.length()];
  /*  //System.out.print("temp = ");
    for(int i = 0; i < setOut.length(); i++){
      if(setOut.charAt(i) == 'A')
        temp[i] = 0;
      else if (setOut.charAt(i)=='T')
        temp[i] = 1;
      else if(setOut.charAt(i) == 'C')
          temp[i] = 2;
      else if (setOut.charAt(i)=='G')
          temp[i] = 3;
      else //invalid
          temp[i] = -1;
      //System.out.print(temp[i]);
    }
      Arrays.sort(temp);

    setOut = ""; */
/*for(int i = 0; i < temp.length; i++){
    //  setOut = setOut + decToDNA(temp[i]);
      System.out.print(setOut.charAt(i));
}*/
    return setOut;
  }

  //combines address; forms 36 addr of length 8
  public static String [] combine(String [] myX, String [] myY){
    String [] myAddr = new String [myX.length * myY.length];
    int index = 0;
    for(int i = 0; i < myY.length; i++) {
      for(int j = 0; j < myX.length; j++) {
        myAddr[index++] = myY[i] + myX[j];
      }
    }
    return myAddr;
  }//combine

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
  public static boolean addrChecker(String in, String addr){
    int addrLen = addr.length();
    int inLen = in.length();
    String window = "";

    int endOfInfo = inLen - 2 * addrLen;
    for (int i = addrLen; i < endOfInfo + addrLen; i++) {
      window = in.substring(i,i +addrLen);
    //  System.out.println(window);
      if(window.equals(addr))
        return false;
    }
    return true;

  } //addrChecker

  //checks constraint 2: GC content
  public static boolean GCchecker(String in){
      double count = 0;
      for (int i = 0; i < in.length(); i++) {
        if(in.charAt(i) == 'C' || in.charAt(i) == 'G')
          count++;
      }
      System.out.println((count / in.length()));
      return  ((count / in.length()) >= 0.48 &&
              (count / in.length()) <= 0.52 );
  } //GCchecker


}
