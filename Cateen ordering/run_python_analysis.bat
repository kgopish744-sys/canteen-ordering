@echo off
title VCET Canteen Ordering System - Python Data Science Pipeline
echo ===============================================================
echo   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (VCET), ERODE
echo         CANTEEN SALES ANALYSIS - PYTHON DATA SCIENCE
echo ===============================================================
echo.
echo Initializing database if needed...
python setup_database.py
echo.
echo Executing Data Science Pipeline (FR 7 & FR 8)...
python data_analysis.py
echo.
echo [DONE] Charts generated in the current folder!
pause
