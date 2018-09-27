#!/bin/sh
#
#Author: wangxueming
#Date: 2018-09-27
#
########################################################
# PROCEDURE
########################################################

if [ ! $# -eq 3 ]; then
        echo "Usage:need three args, command|compareFilePath1|compareFilePath2. " >&2
        exit 1
fi

cmd=$1 
origFilePath=$2 
destFilePath=$3

#JDK所在路径,环境中一般已经配置
#JAVA_HOME="/usr/java/jdk1.7.0_79"
 
#Java应用根目录
APP_HOME=`pwd`

#需要启动的Jar包
APP_MAINCLASS=com.dcits.startup.Bootstrap
 
#拼凑完整的classpath参数，包括指定lib目录下所有的jar
CLASSPATH=$APP_HOME/classes
for i in "$APP_HOME"/lib/*.jar; 
do
   CLASSPATH="$CLASSPATH":"$i"
done

#java虚拟机启动参数
JAVA_OPTS="-Xms512m -Xmx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m"

echo -n "Starting $APP_MAINCLASS ..."
echo
"$JAVA_HOME"/bin/java $JAVA_OPTS -classpath $CLASSPATH  $APP_MAINCLASS "$cmd" "$origFilePath" "$destFilePath" 
