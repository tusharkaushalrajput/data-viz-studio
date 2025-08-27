@echo off
REM Run the application manually without Maven
echo Starting Data Visualization System (Manual Mode)...

REM Check if compiled classes exist
if not exist "target\classes\com\dvs\App.class" (
    echo ERROR: Application not compiled yet.
    echo Please run build-manual.bat or manual-compile.bat first.
    pause
    exit /b 1
)

REM Build classpath with all JAR files
set CLASSPATH=target\classes
if exist "target\lib" (
    for %%i in (target\lib\*.jar) do set CLASSPATH=!CLASSPATH!;%%i
)

REM Set memory options for low-memory systems
REM Ultra low memory JVM settings
set JAVA_OPTS=-Xms8m -Xmx64m -XX:+UseSerialGC -XX:-UseCompressedOops -XX:MaxMetaspaceSize=48m -Dsun.java2d.opengl=false -Dsun.java2d.d3d=false

echo Using classpath: %CLASSPATH%
echo Using Java options: %JAVA_OPTS%
echo.

REM Run the application
java %JAVA_OPTS% -cp "%CLASSPATH%" com.dvs.simple.SimpleApp

if %errorlevel% neq 0 (
    echo.
    echo === Application failed to start ===
    echo Possible issues:
    echo 1. Memory insufficient - try closing other applications
    echo 2. Missing JAR files - check target\lib folder
    echo 3. Compilation errors - run manual-compile.bat again
    echo.
    pause
)

echo Application closed.
pause
