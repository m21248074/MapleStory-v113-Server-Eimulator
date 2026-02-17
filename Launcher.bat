@echo off
title TMS113

set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_172"

if exist "%JAVA_HOME%\bin\javac.exe" (
    set "JAVAC=%JAVA_HOME%\bin\javac.exe"
    set "JAR=%JAVA_HOME%\bin\jar.exe"
	set "JDB=%JAVA_HOME%\bin\jdb.exe"
) else (
    echo [錯誤] 找不到 JDK! 路徑: %JAVA_HOME%
    pause
    exit
)

set CLASSPATH=.;dist\*
java -Xmx512M -server -Dnet.sf.odinms.wzpath=wz  server.ui.ServerLauncher
REM "%JDB%" server.ui.ServerLauncher
pause