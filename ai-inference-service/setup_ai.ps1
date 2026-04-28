Write-Host "🚀 Starting NutriFlow AI Service Setup..." -ForegroundColor Cyan

# 1. Check Python installation
if (!(Get-Command python -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Python not found. Please install Python 3.9+ from python.org" -ForegroundColor Red
    exit
}

# 2. Create Virtual Environment
Write-Host "Creating Virtual Environment (venv)..."
python -m venv venv

# 3. Activate and Install dependencies
Write-Host "Installing dependencies... This may take a few minutes (TensorFlow is large)."
.\venv\Scripts\python.exe -m pip install --upgrade pip
.\venv\Scripts\python.exe -m pip install -r requirements.txt

Write-Host "✅ Setup Complete!" -ForegroundColor Green
Write-Host "To start the service, run:" -ForegroundColor Yellow
Write-Host ".\venv\Scripts\python.exe app.py" -ForegroundColor Yellow
