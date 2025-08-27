@echo off
REM Data Visualization System - Run Script
echo Starting Data Visualization System...

REM Set memory options for low-memory systems
set JAVA_OPTS=-Xms64m -Xmx512m -XX:+UseG1GC

REM Build classpath
set CLASSPATH=target\classes
for %%i in (target\lib\*.jar) do set CLASSPATH=!CLASSPATH!;%%i

REM Run the application
echo Using JAVA_OPTS: %JAVA_OPTS%
java %JAVA_OPTS% -cp "%CLASSPATH%" com.dvs.App

if %errorlevel% neq 0 (
    echo Error starting application. Check memory settings.
    echo Try increasing virtual memory or closing other applications.
    pause
)
