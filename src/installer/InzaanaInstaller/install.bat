@echo off
cls

REM Installing Java (http://www.rgagnon.com/javadetails/java-0642.html)
REM ====================================================================

REM setlocal ENABLEEXTENSIONS
REM set KEY_NAME="HKLM\SOFTWARE\JavaSoft\Java Runtime Environment"
REM set VALUE_NAME=CurrentVersion
::
:: get the current version
::
REM FOR /F "usebackq skip=2 tokens=3" %%A IN (`REG QUERY %KEY_NAME% /v %VALUE_NAME% 2^>nul`) DO (
REM     set ValueValue=%%A
REM )
REM if defined ValueValue (
REM     @echo the current Java runtime is  %ValueValue%
REM ) else (
REM     @echo %KEY_NAME%\%VALUE_NAME% not found.
REM     @echo Installing Java
REM 	jdk-8u102-windows-i586.exe
REM )

REM Installing Xampp (For MySql)
REM =============================

xampp-win32-7.0.9-0-VC14-installer.exe