package com.hackaton.sustaina.data.ml

import android.graphics.Bitmap
import com.hackaton.sustaina.domain.models.DetectionResult
import javax.inject.Inject

class TrashDetectorRepository @Inject constructor(
    private val trashSource: TrashDetectorDataSource
) {

}