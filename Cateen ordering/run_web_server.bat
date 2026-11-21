@echo off
title VCET Canteen Ordering System - Web Server
echo ===============================================================
echo   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (VCET), ERODE
echo         CANTEEN ORDERING SYSTEM - FULLSTACK WEB SERVER
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
echo Starting Java Web Server on http://localhost:8080 ...
echo (Press Ctrl+C to stop the server)
echo.
start http://localhost:8080
java CanteenWebServer
pause
