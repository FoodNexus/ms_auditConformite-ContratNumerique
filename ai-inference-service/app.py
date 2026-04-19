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
    
    # Since we don't have the exact CLASS_NAMES list from training here,
    # we'll return the raw probabilities for the user to help identify the mapping,
    # or implement a smart mapping if class names were saved.
    
    # Note: Keras models sometimes don't save class labels.
    # In a real scenario, we should have a class_names.json.
    
    # Dummy logic for now: if index is high, assume rotten? 
    # Better to return the top index and let the user decide mapping.
    
    prediction_index = int(np.argmax(predictions))
    confidence = float(np.max(predictions))
    
    # IMPORTANT: We'll need to define which indices are "ROTTEN"
    # For now, let's return a response that Java can parse.
    
    # User feedback required to identify which index is which.
    # I'll add a simple heuristic: if index is odd, assume rotten? No, too risky.
    
    return {
        "prediction_index": prediction_index,
        "confidence": confidence,
        "class_count": len(predictions[0])
    }

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
