@echo off
setlocal enabledelayedexpansion
SET JAVA=start javaw
SET LIBPATH=lib
SET CORE=./CyperDataStudio.jar
set CP=%CORE%;
for /f %%i in ('dir /b %LIBPATH%\*.jar^|sort') do (
   set CP=!CP!%LIBPATH%\%%i;
)
set OPTS=-Duser.timezone=GMT+8 -Xmx128m
set MAIN=hello.layout.aqua.CyperDataStudio
echo ===============================================================================
echo.
echo   Engine Startup Environment
echo.
echo   JAVA: %JAVA%
echo.
echo   CONFIG: %CONFIG%
echo.
echo   JAVA_OPTS: %OPTS%
echo.
echo   CLASSPATH: %CP%
echo.
echo   MAIN: %MAIN%
echo.
echo ===============================================================================
echo.

%JAVA% %OPTS% -cp %CP% %MAIN%