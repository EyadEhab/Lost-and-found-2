@echo off
set "BASE=D:\2026\software & design\Lost-and-found-2\Master dcd java"
set "CP=%BASE%\lib\mssql-jdbc-13.2.1.jre8.jar;%BASE%\mssql-jdbc-13.2.1.jre11.jar"
set "BIN=%BASE%\bin"

echo Compiling...
javac -cp "%CP%" -d "%BIN%" @"%BASE%\sources_quoted.txt"
if errorlevel 1 (
    echo Compilation FAILED!
    pause
    exit /b 1
)

echo Compilation successful! Running...
java -cp "%BIN%;%CP%" master.dcd.MasterDcd
pause
