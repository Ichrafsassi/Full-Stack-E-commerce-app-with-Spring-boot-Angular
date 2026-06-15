@echo off
setlocal EnableExtensions
cd /d "%~dp0"
echo NERDS TECH API - http://localhost:8081

for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8081" ^| findstr "LISTENING"') do (
  echo Stopping process on port 8081 PID %%a
  taskkill /F /PID %%a >nul 2>&1
)

call mvnw.cmd spring-boot:run
