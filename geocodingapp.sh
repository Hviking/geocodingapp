#!/bin/bash


# Environment Variables
#
#   JAVA_HOME        The java implementation to use.  Overrides JAVA_HOME.
#



bin=`dirname "$0"`
bin=`cd "$bin"; pwd`




JAVA_HOME=/usr/java/jdk1.7.0_51

# some Java parameters
if [ "$JAVA_HOME" != "" ]; then
  #echo "run java in $JAVA_HOME"
  JAVA_HOME=$JAVA_HOME
fi
  
if [ "$JAVA_HOME" = "" ]; then
  echo "Error: JAVA_HOME is not set."
  exit 1
fi

JAVA=$JAVA_HOME/bin/java
JAVA_HEAP_MAX=-Xmx1000m 


CLASSPATH=$JAVA_HOME/lib/tools.jar

for f in ${bin}/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done
for f in ${bin}/lib/*.jar; do
  CLASSPATH=${CLASSPATH}:$f;
done

 CLASSPATH=${CLASSPATH}:${bin}/log4j.properties

# restore ordinary behaviour
unset IFS


# figure out which class to run
CLASS='com.beyondb.ui.MainJFrame'



exec "$JAVA" $JAVA_HEAP_MAX -classpath "$CLASSPATH"  $CLASS "$@"
