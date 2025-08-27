@echo off
REM Quick test of simplified version
echo Testing simplified Data Visualization System...

REM Change to the project directory
cd /d "%~dp0"

REM Ensure target directory exists
if not exist "target\classes" mkdir target\classes

REM Compile the simplified app with Java 8 target
echo Compiling simplified version for Java 8...
javac -J-Xms16m -J-Xmx64m -source 8 -target 8 -d target\classes -cp target\classes src\main\java\com\dvs\model\DataSet.java src\main\java\com\dvs\simple\SimpleApp.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!

REM Run the simplified app
echo Starting simplified application...
java -Xms8m -Xmx64m -XX:+UseSerialGC -cp target\classes com.dvs.simple.SimpleApp

echo Application closed.
pause
