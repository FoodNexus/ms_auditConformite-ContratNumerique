@echo off
TITLE NutriFlow AI Service
echo --------------------------------------------------
echo 🚀 NutriFlow AI Service - Startup Script
echo --------------------------------------------------

cd /d %~dp0

:: Check if virtual environment exists
if not exist venv (
    echo [!] Virtual environment not found. 
    echo [!] Running first-time setup (installing AI libraries)...
    python -m venv venv
    if errorlevel 1 (
        echo [ERROR] Python not found or failed to create venv. 
        echo Please ensure Python 3.9+ is installed and added to PATH.
        pause
        exit /b
    )
    call .\venv\Scripts\activate
    python -m pip install --upgrade pip
    echo [!] Installing TensorFlow and dependencies (this takes a few minutes)...
    pip install -r requirements.txt
) else (
    echo [OK] Virtual environment found. Activating...
    call .\venv\Scripts\activate
)

echo --------------------------------------------------
echo ✅ AI Engine is now LIVE on http://localhost:8000
echo [!] Keep this window open while using the scanner.
echo --------------------------------------------------
python app.py
pause
