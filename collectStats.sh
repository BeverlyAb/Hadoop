#!/bin/bash
i=0
for file in /Users/fullstackmachine/Desktop/BevCode/EECS_213/P1/Stats/*
do
  prefix="$i.op_class:"
  cache="$i.*demand"

  echo ${file:58}
  grep  "$prefix:total" $file | awk '{print $1,$2}'
  grep "$prefix:MemRead" $file | awk '{print $1, $2}'
  grep "$prefix:MemFloat" $file | awk '{print $1,$2}'
  grep "$prefix:FloatMemWrite" $file | awk '{print $1,$2}'
  grep "$cache.misses" $file | awk '{print $1,$2}'
  grep "$cache.accesses" $file | awk '{print $1,$2}'

  if [ $i -lt 3 ]
  then
    i=$[i + 1]
  else
    i=0
  fi
done
