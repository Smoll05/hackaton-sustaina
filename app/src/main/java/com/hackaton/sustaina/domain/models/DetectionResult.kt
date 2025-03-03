package com.hackaton.sustaina.domain.models

import android.graphics.RectF

data class DetectionResult(
    val label: String,
    val confidence: Float,
    val boundingBox: RectF? = null
)