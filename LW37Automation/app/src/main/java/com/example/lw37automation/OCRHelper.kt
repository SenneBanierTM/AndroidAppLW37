package com.example.lw37automation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object OCRHelper {

    suspend fun extractText(imagePath: String): String {
        return withContext(Dispatchers.IO) {
            // Simulate OCR logic
            "Sample extracted text from $imagePath"
        }
    }
}
