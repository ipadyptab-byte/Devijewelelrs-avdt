@echo off
REM ==========================================
REM Showroom Music - Windows Build Script
REM ==========================================
REM 
REM Usage:
REM   1. Double-click this file to build
REM   2. Or run: build-windows.bat
REM
REM Requirements:
REM   - Java 21+ installed
REM   - JAVA_HOME set (or Java in PATH)
REM

echo ==========================================
echo   Showroom Music - Windows Build
echo ==========================================
echo.

REM Check Java
echo [1/3] Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java not found!
    echo.
    echo Please install Java 21 or later:
    echo   https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/
    echo.
    echo After installing, run this script again.
    echo.
    pause
    exit /b 1
)
echo OK - Java found
echo.

REM Check gradlew
echo [2/3] Checking Gradle wrapper...
if not exist "gradlew.bat" (
    echo ERROR: gradlew.bat not found!
    echo.
    echo Make sure you're running this from the project folder.
    echo.
    pause
    exit /b 1
)
echo OK - Gradle wrapper found
echo.

REM Build
echo [3/3] Building Windows application...
echo.
echo This may take several minutes on first run (downloading dependencies).
echo.

call gradlew.bat :desktopApp:package

if errorlevel 1 (
    echo.
    echo ==========================================
    echo   BUILD FAILED
    echo ==========================================
    echo.
    echo Check the error messages above for details.
    echo.
    pause
    exit /b 1
)

echo.
echo ==========================================
echo   BUILD SUCCESSFUL!
echo ==========================================
echo.
echo Output location:
echo   desktopApp\build\output\compose\
echo.
echo To run the app:
echo   desktopApp\build\output\compose\desktopApp.exe
echo.

REM List the exe
dir /b "desktopApp\build\output\compose\*.exe" 2>nul
if errorlevel 1 (
    echo Note: .exe not found in expected location.
    echo Check: desktopApp\build\output\
)

echo.
pause
