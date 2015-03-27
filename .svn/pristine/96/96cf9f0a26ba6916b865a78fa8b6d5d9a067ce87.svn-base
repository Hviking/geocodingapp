@echo off

REM Environment Variables
REM 
REM    JAVA_HOME        The java implementation to use.  Overrides JAVA_HOME.
REM 


REM 切换到bat所在的目录
set bin=%~dp0
cd %bin% 

REM if no args specified, show usage
:LOOP
IF [%1]==[] GOTO END
REM 保存参数，以做回归分析
SET PARAMETER=%PARAMETER% %1 
REM 此处插入其他参数分析代码
SHIFT
:GOTO LOOP




if [%1]==[] (
echo "用法: rasterTool  COMMAND [args....] [options] 
        COMMAND是下面列表中的一项:
          rasterImportWin                                                     栅格批量并行导入工具窗口
          rasterImportWin  idataMainfest idbFile logFile         栅格批量并行导入工具窗口(有界面快速导入)
          rasterImport  idataMainfest idbFile logFile               栅格批量并行导入工具(命令行快速导入)

          rasterExportWin                                                     栅格批量并行导出工具窗口
          rasterExportWin  logFile  odbFile paramPath StorePath   栅格批量并行导出工具窗口(有界面快速导出)
          rasterExport   logFile  odbFile paramPath StorePath        栅格批量并行导出工具(命令行快速导出)
  
         默认支持的文件格式是tiff;tif;img;bmp;TGC;EGC
         其中选项包括:
           -t           <整数值,访问数据时间，单位是分钟>   
           -Tnum	   <整数值,线程池大小,默认20>  
           -Tlimit	   <整数值,设置同时运行线程个数，与p参数互斥,须搭配t参数,此时参数文件名必须是数字，限导出使用>  
           -p           <文件序号，字符类型，限导出使用>  
           -i	           <无值,设置与指定导出范围的关系是范围内导出，否则相交的所有图幅都整幅导出，限导出使用>  
           -f           <输出文件记录因子，导出序号是输出文件记录因子整数倍的图层，限导出使用>  
           -of           <指定导出文件格式{BMP,ENVI,GIF,GTiff,HFA,JPEG}中的一种,限导出使用>
           -tbl_prefix  <指定表前缀的值，栅格数据在数据库中存储的表命名为“前缀”+“_”+“比例尺”，默认前缀是“image”>  
           -pl          <指定金字塔级数,默认导入6级金字塔>  

           -I	           <设置像元交错类型BSQ，BIP，BIL，默认BSQ，限导入使用>
           -M           <指定栅格数据的存储模式inline,outline,gateway,限导入使用,当前inline,outline可用>  
           -O           <指定已有栅格数据是否覆盖,无值,限导入使用>  
           -if           <filter,指定可以识别的的文件格式，以；隔开,
                                      如\"tiff;tif;img\",默认支持\"tiff;tif;img;bmp;TGC;EGC;jpg;jpeg\",限导入使用>  
           -b           <指定栅格块宽、高、波段数，默认是(512,512,3),限导入使用>
           -ex          <对于没有后缀名的栅格文件指定后缀名，如\"tif\",限导入使用>  
           -n           <指定OUTLINE模式的存储路径,限导入使用>  
           -first       <设置是否是第一次导入，初始导入时将不再从数据库中检测数据是否已存在,
                                      同时设置—O参数时，以-first为准，不再检测数据，限导入使用>
           -transform       <设置是否开启仿射变换,前提是导入文件名必须是新图幅号，限导入使用>
           -s           <SRID,指定要栅格参数文件经纬度坐标所采用的坐标系ID>
         参数包括：
           idataMainfest           <存储栅格数据清单配置文件的绝对路径>
           idbFile                      <目标数据库配置文件的绝对路径>
           logFile                                   <指定日志文件>
           odbFile                             <待导出数据库配置文件>
           paramPath                         <待导出栅格参数文件路径>
           StorePath                       <指定导出栅格文件存储路径>
        例如：
        rasterTool rasterExport  /home/testnv/rasterExport_log.txt /home/testnv/projects/RasterExportConfig/exportRaster.xml /home/testnv/data/concurrent /home/testnv/音乐 -t 20 -Tnum 20 -p 1
"
goto END
)

  

REM 获取第一个参数

set COMMAND=%1

:: set Java parameters
set JAVA_HOME=%cd%\jdk1.7.0_51
if not "%JAVA_HOME%" == "" (
:: 设置环境变量的地址
    JAVA_HOME=%JAVA_HOME%
)

  
if  "%JAVA_HOME%" = "" (
  echo "Error: JAVA_HOME is not set."
  exit 1
)

set JAVA=%JAVA_HOME%\bin\java
set JAVA_HEAP_MAX=-Xmx1000m 


set CLASSPATH=%JAVA_HOME%\lib\tools.jar

for /R "%bin%" %%s in ('dir /b /a-d /s "*.jar"') do (
::遍历当前目录下的所有文件
echo %%s
CLASSPATH=%CLASSPATH%;%%s
) 

for /R "%bin%\lib" %%s in ('dir /b /a-d /s "*.jar"') do (
::遍历\lib下的所有文件
echo %%s
CLASSPATH=%CLASSPATH%;%%s
) 


CLASSPATH=%CLASSPATH%;%bin\log4j.properties


set CLASS='com.beyondb.ui.MainJFrame'
REM figure out which class to run
if  "%COMMAND%" = "Main" ] ( then
  CLASS='com.beyondb.ui.MainJFrame'
)

start "%JAVA%" %JAVA_HEAP_MAX% -classpath "%CLASSPATH%"  %CLASS% 

:END