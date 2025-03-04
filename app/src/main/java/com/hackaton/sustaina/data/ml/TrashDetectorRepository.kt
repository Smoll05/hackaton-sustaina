package com.hackaton.sustaina.data.ml

import android.graphics.Bitmap
import com.hackaton.sustaina.domain.models.DetectionResult
import javax.inject.Inject

class TrashDetectorRepository @Inject constructor(
    private val trashSource: TrashDetectorDataSource
) {
//    fun analyzeImage(bitmap: Bitmap) : List<DetectionResult> {
//        val results = trashSource.detectTrash(bitmap).map { detection ->
//            DetectionResult(
//                label = detection.label, // YOLO class name
//                confidence = detection.score,  // Confidence score (0.0 - 1.0)
////                boundingBox = detection.count // Coordinates of detected object
//            )
//        }
//        return results
//    }

    fun analyzeImage(bitmap: Bitmap) {
//        trashSource.detectTrash(bitmap)
        trashSource.detectTrash();
    }
}