#! /bin/sh
JAVA_HOME=/usr/java8_64/jre/bin
export JAVA_HOME
          
CLASSPATH=/home/ldvalle/locks/java/SALT/FuseBatch/bin
CLASSPATH=${CLASSPATH}:/home/ldvalle/locks/java/SALT/FuseBatch/libs/ifxjdbc-4.10.9.jar
CLASSPATH=${CLASSPATH}:/home/ldvalle/locks/java/SALT/FuseBatch/libs/commons-codec-1.14.jar

export CLASSPATH

nohup $JAVA_HOME/java -Xmx1024m -cp $CLASSPATH ppal.FuseStart "UNIX" >fuseStart.log 2>fuseStart.err &
