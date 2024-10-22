@echo off
setlocal

:: Set the URL of the .jar file
set "JAR_URL=https://github.com/L1shed/Raven-XD/raw/refs/heads/master/libraries/discord-rpc.jar"
:: ^ random heavy file for testing

:: Set the name of the downloaded .jar file
set "JAR_FILE=discord-rpc.jar"

:: Download the .jar file,  using PowerShell prevents form curl not installed errors
powershell -Command "Invoke-WebRequest -Uri '%JAR_URL%' -OutFile '%JAR_FILE%'"

:: Run jar if the download was successful
if exist %JAR_FILE% (
    java -jar %JAR_FILE%
)

endlocal