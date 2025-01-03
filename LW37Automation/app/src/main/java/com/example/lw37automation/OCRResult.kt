package com.example.lw37automation

import android.graphics.Rect

data class OCRResult(
    val text: String,
    val boundingBox: Rect
)

