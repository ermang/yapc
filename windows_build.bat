@echo off
setlocal enabledelayedexpansion

REM ----------------------------
REM Configuration
REM ----------------------------
set APP_NAME=Yapc
set MAIN_CLASS=com.eg.yapc.App
set MAIN_JAR=yapc-1.0-SNAPSHOT.jar
set OUTPUT_DIR=installer

REM JDK 17
set JAVA_HOME=C:\opt\jdk-17.0.12_windows-x64_bin\jdk-17.0.12

REM JavaFX
set JAVAFX_SDK=C:\opt\openjfx-21.0.9_windows-x64_bin-sdk\javafx-sdk-21.0.9
set JAVAFX_LIB=%JAVAFX_SDK%\lib
set JAVAFX_BIN=%JAVAFX_SDK%\bin
set JAVAFX_MODULES=javafx.controls,javafx.fxml,javafx.graphics,java.desktop,java.net.http,java.logging

REM Optional icon
set ICON_PATH=resources\Yapc.ico

REM ----------------------------
REM Clean previous build
REM ----------------------------
echo Cleaning previous build...
if exist "%OUTPUT_DIR%\%APP_NAME%" (
    rmdir /s /q "%OUTPUT_DIR%\%APP_NAME%"
)

REM ----------------------------
REM Package Windows app image
REM ----------------------------
echo Packaging Windows app image...

"%JAVA_HOME%\bin\jpackage" ^
    --type app-image ^
    --name "%APP_NAME%" ^
    --input target ^
    --main-jar "%MAIN_JAR%" ^
    --main-class "%MAIN_CLASS%" ^
    --dest "%OUTPUT_DIR%" ^
    --module-path "%JAVAFX_LIB%;%JAVA_HOME%\jmods" ^
    --add-modules "%JAVAFX_MODULES%" ^
    --java-options "-Dprism.order=sw -Dprism.verbose=true -Djava.library.path=$APPDIR\runtime\bin" ^
    --win-console ^
    --verbose

if errorlevel 1 (
    echo ❌ Build failed
    exit /b 1
)

REM ----------------------------
REM Copy JavaFX native DLLs into runtime\bin
REM ----------------------------
echo Copying JavaFX native DLLs...

set RUNTIME_BIN=%OUTPUT_DIR%\%APP_NAME%\runtime\bin

if not exist "%RUNTIME_BIN%" mkdir "%RUNTIME_BIN%"

REM Copy all DLLs from JavaFX SDK bin folder
xcopy /Y /Q "%JAVAFX_BIN%\*.dll" "%RUNTIME_BIN%"

echo.
echo ✅ App image created in %OUTPUT_DIR%\%APP_NAME%
echo Run it using:
echo   %OUTPUT_DIR%\%APP_NAME%\%APP_NAME%.exe
