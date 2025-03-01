package com.hackaton.sustaina.data.camera

import java.io.File
import javax.inject.Inject

class CameraRepository @Inject constructor(
    private val cameraDataSource: CameraDataSource
) {
    var lastImageSaved: File? = null
}