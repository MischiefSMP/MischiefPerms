@echo off
setlocal enabledelayedexpansion
cls

REM This make file is not required to build. If you just want the jar just use gradle
REM I use this for easier setup with my test server

REM I Recommend this following code as your start.bat in this case

REM cd %~dp0
REM java -jar paper.jar -nogui
REM exit

set serverFolder=Server Folder here
set pluginFolder=%serverFolder%\plugins
set startFile="start.bat"
set projectName=MischiefPerms

IF "%~1" == "" goto help

if %1==help goto help
if %1==clean goto clean
if %1==build goto build
if %1==run goto run
if %1==full goto full
if %1==open goto open

:help
echo help:
echo make clean ^> Cleans temporary build files and the jar file and folder
echo make build ^> Builds and moves the jar into the plugin folder
echo make run   ^> Starts the server
echo make full  ^> Executes make build and make run
echo make open  ^> Opens the plugin folder
goto done

:clean
echo Cleaning...
call gradlew clean >> nul

if exist %pluginFolder%\%projectName%.jar (
    del %pluginFolder%\%projectName%.jar
)

if exist %pluginFolder%\%projectName%\ (
    rmdir /Q /S %pluginFolder%\%projectName%
)

echo Done!
goto :done

:build
echo Building jar
call gradlew build >> nul
echo Moving jar
xcopy /S /Q /Y /F build\libs\%projectName%.jar %pluginFolder%\%projectName%.jar* >> nul
echo Done!
goto :EOF

:run
echo Starting server
start call %serverFolder%\%startFile%
goto :EOF

:full
call :build
call :run
goto :done

:open
echo %pluginFolder%
if exist %pluginFolder%\ (
    %SystemRoot%\explorer.exe %pluginFolder%
) else (
    echo Folder not found
)
goto done

:done