package com.hackaton.sustaina.ui.camera

import java.io.File

sealed class CameraState {
    data object Idle : CameraState()
    data object Loading : CameraState()
    data class Success(val image : File?) : CameraState()
    data class Error(val msg : String?) : CameraState()
}