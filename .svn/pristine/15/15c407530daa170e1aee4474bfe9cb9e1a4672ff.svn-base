@echo off

REM Environment Variables
REM 
REM    JAVA_HOME        The java implementation to use.  Overrides JAVA_HOME.
REM 


REM change directory 
set bin=%~dp0
cd %bin% 


:: set Java parameters
setlocal 
REM setx  JAVAHOME "C:\Program Files\Java\jdk1.6.0_43\jdk1.7.0_51"
REM if not %JAVA_HOME% == "" (
REM :: set java environment 
REM JAVAHOME=%JAVA_HOME%
REM echo %JAVA_HOME%
REM )
 
REM if  '%JAVA_HOME%' = ''(
  REM echo "Error: JAVA_HOME is not set."
  REM exit 1
REM )

set JAVA=%JAVA_HOME%\bin\java
set JAVA_HEAP_MAX=-Xmx1000m 


REM set CLASSPATH=%JAVA_HOME%\lib\tools.jar

REM for /R "%bin%" %%s in ('dir /b /a-d /s "*.jar"') do (
REM echo %%s
REM ::CLASSPATH=%CLASSPATH%;%%s
REM ) 

REM for /R "%bin%\lib" %%s in ('dir /b /a-d /s "*.jar"') do (
REM echo %%s
REM CLASSPATH=%CLASSPATH%;%%s
REM ) 


REM CLASSPATH=%CLASSPATH%;%bin\log4j.properties

REM figure out which class to run
set CLASS=com.beyondb.ui.MainJFrame


::start "%JAVA%" %JAVA_HEAP_MAX% -classpath "%CLASSPATH%"  %CLASS% 
java %JAVA_HEAP_MAX% -jar geocodingapp.jar  %CLASS% 
