package com.hackaton.sustaina.data.camera

import java.io.File
import javax.inject.Inject

class CameraRepository @Inject constructor(
    private val cameraDataSource: CameraDataSource
) {
    private var lastImageSaved: File? = null

    fun setLastImageSaved(image: File) {
        lastImageSaved = image
    }
}