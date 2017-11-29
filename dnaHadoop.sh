#!/bin/bash
hdfs dfs -rm -r  dnaOut
rm -r dnaOut.txt
javac -cp `hadoop classpath` DNA_MR.java
jar cvfm DNA_MR.jar manifest.txt *.class
hadoop jar DNA_MR.jar allAddr.txt dnaOut the
hdfs dfs -copyToLocal /user/fullstackmachine/dnaOut/part-r-00000 /Users/fullstackmachine/Desktop/BevCode/EECS_298/projects/p2/dnaOut.txt
cat dnaOut.txt
