@echo off
echo Compiling minimal runner with very small heap...
if not exist target\classes mkdir target\classes
javac -J-Xms16m -J-Xmx64m -source 8 -target 8 -d target\classes src\main\java\com\dvs\model\DataSet.java src\main\java\com\dvs\simple\MiniRun.java
if %errorlevel% neq 0 (
  echo Compile failed.
  pause
  exit /b 1
)
echo Running MiniRun...
java -Xms8m -Xmx48m -XX:+UseSerialGC -cp target\classes com.dvs.simple.MiniRun
echo Done.
pause
