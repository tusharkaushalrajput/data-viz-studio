@echo off
REM Data Visualization System - Package for Distribution
echo Creating distribution package...

REM Clean and build
call mvn clean package

REM Create distribution directory
if exist "dist" rmdir /s /q dist
mkdir dist
mkdir dist\lib
mkdir dist\data
mkdir dist\exports

REM Copy files
copy target\datavizsys-1.0-SNAPSHOT.jar dist\datavizsys.jar
copy target\lib\*.jar dist\lib\
copy run.bat dist\
copy README.md dist\ 2>nul
copy LICENSE dist\ 2>nul

REM Create sample data
echo Product,Sales,Quarter > dist\data\sample-data.csv
echo Laptops,1500,Q1 >> dist\data\sample-data.csv
echo Phones,2300,Q1 >> dist\data\sample-data.csv
echo Tablets,800,Q1 >> dist\data\sample-data.csv
echo Laptops,1800,Q2 >> dist\data\sample-data.csv
echo Phones,2100,Q2 >> dist\data\sample-data.csv

REM Create installation guide
echo # Data Visualization System > dist\INSTALL.txt
echo. >> dist\INSTALL.txt
echo ## Installation: >> dist\INSTALL.txt
echo 1. Ensure Java 8+ is installed >> dist\INSTALL.txt
echo 2. Double-click run.bat to start the application >> dist\INSTALL.txt
echo 3. Or run: java -jar datavizsys.jar >> dist\INSTALL.txt
echo. >> dist\INSTALL.txt
echo ## Sample Data: >> dist\INSTALL.txt
echo - Load sample-data.csv from the data folder >> dist\INSTALL.txt
echo - Try different chart types (Bar, Pie, Line, Scatter) >> dist\INSTALL.txt

echo Distribution package created in 'dist' folder!
echo You can now zip the 'dist' folder for distribution.
pause
