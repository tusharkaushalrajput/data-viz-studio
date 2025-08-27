@echo off
REM Data Visualization System - Manual Build Script (No Maven Required)
echo Building Data Visualization System manually...

REM Create directories
if not exist "target\classes" mkdir target\classes
if not exist "target\lib" mkdir target\lib
if not exist "dist" mkdir dist

echo.
echo === Step 1: Creating directory structure ===
echo Directories created successfully.

echo.
echo === Step 2: Downloading required JAR files ===
echo You need to manually download these JAR files to target\lib\:
echo.
echo 1. JFreeChart JAR files:
echo    - Download from: https://github.com/jfree/jfreechart/releases
echo    - Files needed: jfreechart-1.5.3.jar, jcommon-1.0.24.jar
echo.
echo 2. OpenCSV JAR file:
echo    - Download from: https://repo1.maven.org/maven2/com/opencsv/opencsv/5.7.1/
echo    - File needed: opencsv-5.7.1.jar
echo.
echo 3. Apache Commons JAR files:
echo    - commons-lang3-3.12.0.jar
echo    - commons-io-2.11.0.jar
echo    - commons-beanutils-1.9.4.jar
echo    - commons-logging-1.2.jar
echo    - commons-collections-3.2.2.jar
echo.
echo OR use the simplified version without external dependencies...

choice /C YN /M "Do you want to create a simplified version without external dependencies (Y/N)?"

if errorlevel 2 goto :manual_download
if errorlevel 1 goto :simplified_build

:simplified_build
echo.
echo === Creating simplified version ===
echo Compiling core classes without external dependencies...

REM Compile DataSet model (no external dependencies)
javac -d target\classes src\main\java\com\dvs\model\DataSet.java
if %errorlevel% neq 0 (
    echo Error compiling DataSet.java
    goto :error
)

REM Compile simplified test
javac -d target\classes -cp target\classes src\main\java\com\dvs\test\SimpleTest.java
if %errorlevel% neq 0 (
    echo Error compiling SimpleTest.java
    goto :error
)

echo.
echo === Testing core functionality ===
echo Running basic test...
java -Xms32m -Xmx128m -cp target\classes com.dvs.test.SimpleTest

echo.
echo === Simplified build completed! ===
echo You can now run: java -cp target\classes com.dvs.test.SimpleTest
goto :end

:manual_download
echo.
echo === Manual Download Instructions ===
echo.
echo Please download the required JAR files to target\lib\ folder:
echo.
echo 1. Create target\lib folder if it doesn't exist
echo 2. Download all JAR files listed above
echo 3. Run manual-compile.bat after downloading
echo.
pause
goto :end

:error
echo.
echo === Build Failed ===
echo Please check the error messages above.
echo Make sure Java compiler is available in PATH.
pause
goto :end

:end
echo.
echo Build process completed.
pause
