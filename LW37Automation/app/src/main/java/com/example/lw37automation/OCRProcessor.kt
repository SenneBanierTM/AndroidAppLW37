package com.example.lw37automation

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await

object OCRProcessor {

    suspend fun extractText(bitmap: Bitmap): String? {
        return try {
            // Convert the Bitmap to an InputImage
            val image = InputImage.fromBitmap(bitmap, 0) // Ensure bitmap is non-null

            // Get the Text Recognition Client with Latin options
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            // Process the image and wait for the result
            val result = recognizer.process(image).await()

            // Return the recognized text
            result.text
        } catch (e: Exception) {
            Log.e("OCRProcessor", "Error during text extraction", e)
            null
        }
    }
}
