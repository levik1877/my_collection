@echo off
setlocal
cd /d "%~dp0"
if not exist "node_modules\vite\bin\vite.js" (
  echo Install dependencies first: npm install
  pause
  exit /b 1
)
where node >nul 2>nul
if not errorlevel 1 (
  node "node_modules\vite\bin\vite.js" --host 127.0.0.1
  exit /b
)
set "MYCOLLECTION_NODE=%USERPROFILE%\.cache\codex-runtimes\codex-primary-runtime\dependencies\node\bin\node.exe"
if exist "%MYCOLLECTION_NODE%" (
  "%MYCOLLECTION_NODE%" "node_modules\vite\bin\vite.js" --host 127.0.0.1
  exit /b
)
echo Node.js 22.12 or newer is required.
pause
exit /b 1
