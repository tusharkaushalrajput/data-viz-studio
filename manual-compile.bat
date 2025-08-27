@echo off
REM Manual compilation after JAR files are downloaded
echo Compiling with downloaded JAR files...

REM Check if JAR files exist
if not exist "target\lib\*.jar" (
    echo ERROR: No JAR files found in target\lib\
    echo Please download the required JAR files first.
    echo See build-manual.bat for download instructions.
    pause
    exit /b 1
)

echo Found JAR files:
dir target\lib\*.jar

REM Build classpath
set CLASSPATH=target\classes
for %%i in (target\lib\*.jar) do set CLASSPATH=!CLASSPATH!;%%i

echo.
echo Classpath: %CLASSPATH%

echo.
echo === Compiling Java files ===

REM Compile in dependency order
echo Compiling DataSet...
javac -d target\classes -cp "%CLASSPATH%" src\main\java\com\dvs\model\DataSet.java

echo Compiling DataService...
javac -d target\classes -cp "%CLASSPATH%" src\main\java\com\dvs\service\DataService.java

echo Compiling ChartService...
javac -d target\classes -cp "%CLASSPATH%" src\main\java\com\dvs\service\ChartService.java

echo Compiling UI classes...
javac -d target\classes -cp "%CLASSPATH%" src\main\java\com\dvs\ui\DataEntryDialog.java
javac -d target\classes -cp "%CLASSPATH%" src\main\java\com\dvs\ui\MainWindow.java

echo Compiling main App...
javac -d target\classes -cp "%CLASSPATH%" src\main\java\com\dvs\App.java

if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo === Compilation successful! ===
echo You can now run: run-manual.bat
pause
