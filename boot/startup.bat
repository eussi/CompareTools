::Program��
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

::ͳ�Ʋ�������
set /a cnt=0
:loop
if not "%1"=="" (set /a cnt+=1&shift /1&goto :loop)

if %cnt% NEQ 3 (
	echo Usage:need three args, command^|compareFilePath1^|compareFilePath2. 
	goto exit
)

::JavaӦ�ø�Ŀ¼
set APP_HOME=%cd%

::��Ҫ������Java��
set APP_MAINCLASS=com.eussi.startup.Bootstrap

::classpath����������ָ��libĿ¼�����е�jar
set CLASSPATH=%APP_HOME%\classes

For /r "%APP_HOME%\lib" %%f in (*.jar) do (
	set CLASSPATH=!CLASSPATH!;%%f
)

::java�������������, MaxPermSize=128m; support was removed in 8.0
set JAVA_OPTS=-Xms512m -Xmx512m -Xmn256m -Djava.awt.headless=true -XX:MaxPermSize=128m

echo Starting %APP_MAINCLASS% ...
echo.
"%JAVA_HOME%\bin\java" -classpath %CLASSPATH% %JAVA_OPTS% %APP_MAINCLASS% %cmd% %origFilePath% %destFilePath% 

:exit
echo .
echo Exit after 3 seconds
choice /t 3 /d y >nul