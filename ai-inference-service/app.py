import os
import numpy as np
from fastapi import FastAPI, File, UploadFile
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing import image
from io import BytesIO
from PIL import Image

app = FastAPI()

# Configuration
MODEL_PATH = "models/fruit_freshness_model.keras"
IMG_SIZE = (224, 224)

# Load model
print(f"Loading model from {MODEL_PATH}...")
model = load_model(MODEL_PATH)
print("Model loaded successfully!")

# Class names based on the Fruits_Vegetables_Dataset structure
CLASS_NAMES = [
    "FreshApple", "FreshBanana", "FreshBellpepper", "FreshCarrot", "FreshCucumber",
    "FreshMango", "FreshOrange", "FreshPotato", "FreshStrawberry", "FreshTomato",
    "RottenApple", "RottenBanana", "RottenBellpepper", "RottenCarrot", "RottenCucumber",
    "RottenMango", "RottenOrange", "RottenPotato", "RottenStrawberry", "RottenTomato"
]

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    # Read and preprocess the image
    contents = await file.read()
    img = Image.open(BytesIO(contents)).convert('RGB')
    img = img.resize(IMG_SIZE)
    
    img_array = image.img_to_array(img) / 255.0
    img_array = np.expand_dims(img_array, axis=0)
    
    # Predict
    predictions = model.predict(img_array)
    
    prediction_index = int(np.argmax(predictions))
    confidence = float(np.max(predictions))
    class_name = CLASS_NAMES[prediction_index] if prediction_index < len(CLASS_NAMES) else "Unknown"
    
    return {
        "prediction_index": prediction_index,
        "class_name": class_name,
        "confidence": confidence,
        "class_count": len(predictions[0])
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
