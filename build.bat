@echo off
REM Data Visualization System - Build Script
echo Building Data Visualization System...

REM Create directories
if not exist "target\classes" mkdir target\classes
if not exist "target\lib" mkdir target\lib

REM Download dependencies (if Maven not available)
echo Checking for Maven...
mvn --version >nul 2>&1
if %errorlevel% == 0 (
    echo Maven found, using Maven build...
    call mvn clean compile
    call mvn dependency:copy-dependencies -DoutputDirectory=target\lib
) else (
    echo Maven not found, manual setup required...
    echo Please install Maven or use manual compilation steps
)

echo Build completed!
pause
