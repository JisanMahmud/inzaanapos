@echo off
cls

REM Installing Java

@echo Checking If Java is Installed
setlocal ENABLEEXTENSIONS
set KEY_NAME="HKLM\SOFTWARE\JavaSoft\Java Runtime Environment"
set VALUE_NAME=CurrentVersion
::
:: get the current version
::
FOR /F "usebackq skip=2 tokens=3" %%A IN (`REG QUERY %KEY_NAME% /v %VALUE_NAME% 2^>nul`) DO (
    set ValueValue=%%A
)
if defined ValueValue (
    @echo the current Java runtime is  %ValueValue%
) else (
	@echo Java is not Installed. Installing Java 1.8
    REM 
)


REM Installing Xmpp
@echo Installing Database for onlyne
REM
