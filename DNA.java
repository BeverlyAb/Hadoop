public class DNA {
  static String [] x = {"ATCG", "ATAG", "CTAG", "CTCG", "ATTG", "CTTG"};
  static String [] y = {"ACCG", "AAAG", "CAAG", "CCCG", "ATTT", "CTTT"};

  public static void main(String [] args) {

      /*System.out.println(args[0]);
      System.out.println(args[1]);
      if (addrChecker(args[0],args[1]))
        System.out.println("good");
      else
        System.out.println("bad"); */
      String allAddr [] = combine(x,y);
      for(int i = 0; i < allAddr.length; i++) {
        System.out.print(i + ": ");
        System.out.println(allAddr[i]);
      }

  }


  //combines address; forms 36 addr of length 8
  public static String [] combine(String [] myX, String [] myY){
    String [] myAddr = new String [myX.length * myY.length];
    int index = 0;
    for(int i = 0; i < myY.length; i++) {
      for(int j = 0; j < myX.length; j++) {
        myAddr[index++] = myY[i] + myX[j];
          System.out.println(myAddr[i]);
      }
    }
    return myAddr;
  }//combine

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
