@echo off
title VCET Canteen Ordering System - Standalone Java App
echo ===============================================================
echo   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (VCET), ERODE
echo         CANTEEN ORDERING SYSTEM - JAVA APPLICATION
echo ===============================================================
echo.
echo Compiling Java source files...
javac *.java
if %errorlevel% neq 0 (
    echo [ERROR] Java compilation failed! Please check JDK installation.
    pause
    exit /b %errorlevel%
)
echo Compilation successful!
echo.
echo Launching Interactive Canteen Console Application...
echo.
java CanteenApp
pause
