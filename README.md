# CompareTools

##### 1、功能说明：
1. 首先应用目录下configs/entries.xml配置文件，由多条entry组成，每一个entry由一个命令和一系列处理流程组成。程序启动后，程序根据启动类传入的命令参数依次执行此文件配置的每一个处理流程
2. 每一个处理流程继承抽象类com.dcits.core.impl.ValveBase,实现invokeHook(String orig, String dest, IValveContext context)抽象方法实现处理过程
3. invokeHook接收三个参数，两个文件路径参数，和一个上下文参数，对两个文件或文件夹的对比等工作在invokeHook方法中实现，上下文参数可以储存当前处理流程的处理结果，供给下一个处理流程使用
4. 后续需求只需实现该处理流程，并在configs/entries.xml中添加一条entry配置即可

#####  2、使用方式：
1. 按照以下步骤操作，首先了解后续介绍的部署应用目录结构
2. 查看应用目录下configs/entries.xml配置文件，已经实现了cmp_flow、cmp_esb、cmp_test命令
3. 在linux环境中，运行 sh startup.sh cmp_test path1 path2 即可
4. 在windows环境中，打开cmd命令窗口，运行 startup.bat cmp_test path1 path2 即可

##### 3、jdk:
1.7.0_79

##### 4、依赖jar包（前两个jar包已经打进核心包，第三个是解析tar文件所需jar包）:

* dom4j-1.6.1.jar
* jaxen-1.1.6.jar
* commons-compress-1.18.jar

##### 5、已实现处理流程介绍：
* com.dcits.compare.tar.commonflow.ExtractTarValve 将传入的两个tar包解压，如果加入此流程，后续的commonflow均对比解压后文件，此目的提高效率，但是解压会占用空间
* com.dcits.compare.tar.commonflow.FileNumNameValve 比较传入的两个tar包中文件名是否一致，并将差异列出
* com.dcits.compare.tar.commonflow.FileDiffShowValve 比较传入的两个tar包中文件内容是否一致，并将差异文件名列出
* com.dcits.compare.tar.commonflow.FileContentDiffShowValve 列出两个tar包中文件内容的差异
* com.dcits.compare.tar.flowctrlflow.FlowConfDiffValve 列出流控jar包中conf_flow,conf_flow1中文件名是否一致，并将差异列出
* com.dcits.compare.tar.flowctrlflow.FlowConfContentDiffValve 列出流控tar包中conf_flow,conf_flow1中文件内容差异
* com.dcits.compare.test.TestValve 测试流程
* com.dcits.compare.test.TestValve2 测试流程

##### 6、脚本启动：
1. 目前将核心代码打包为CompareTools_1.0_release.jar，启动时引用即可（只包含实现了cmp_test的代码，后续需求class放在classes目录，并在configs/entries.xml中添加一条entry配置即可）
2. 部署应用目录结构

> lib 程序需要的jar包

>> CompareTools_1.0_release.jar 核心jar包

>> 其他需要jar包....

> classes 后续需求添加的classes文件位置

> configs 配置文件目录

>> entries.xml 核心配置文件

> startup.sh linux环境启动脚本，注意文件是linux格式，编码为utf-8

> startup.bat windows环境启动脚本，注意文件是PC格式，编码为GBK

3. 脚本内容：

**startup.sh**
```
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

```

**startup.bat**
```
::Program：
::	CompareTools
::Author:
::	wangxueming
::Date:
::	2019-09-27 First Release

@echo off
title CompareTools
setlocal enabledelayedexpansion
cls

set cmd=%1%
set origFilePath=%2%
set destFilePath=%3%

::统计参数个数
set /a cnt=0
:loop
if not "%1"=="" (set /a cnt+=1&shift /1&goto :loop)

if %cnt% NEQ 3 (
	echo Usage:need three args, command^|compareFilePath1^|compareFilePath2. 
	goto exit
)

::Java应用根目录
set APP_HOME=%cd%

::需要启动的Java类
set APP_MAINCLASS=com.dcits.startup.Bootstrap

::classpath参数，包括指定lib目录下所有的jar
set CLASSPATH=%APP_HOME%\classes

For /r "%APP_HOME%\lib" %%f in (*.jar) do (
	set CLASSPATH=!CLASSPATH!;%%f
)

::java虚拟机启动参数, MaxPermSize=128m; support was removed in 8.0
set JAVA_OPTS=-Xms512m -Xmx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m

echo Starting %APP_MAINCLASS% ...
echo.
"%JAVA_HOME%\bin\java" -classpath %CLASSPATH% %JAVA_OPTS% %APP_MAINCLASS% %cmd% %origFilePath% %destFilePath% 

:exit
echo .
echo Exit after 3 seconds
choice /t 3 /d y >nul
```
