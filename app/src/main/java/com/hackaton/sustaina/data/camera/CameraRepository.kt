package com.hackaton.sustaina.data.camera

import android.graphics.Bitmap
import java.io.File
import javax.inject.Inject

class CameraRepository @Inject constructor(
    private val cameraDataSource: CameraDataSource
) {
    var lastFileSaved: File? = null
}