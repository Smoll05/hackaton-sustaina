package com.hackaton.sustaina.ui.camera

import android.graphics.Bitmap

sealed class CameraState {
    data object Idle : CameraState()
    data object Loading : CameraState()
    data class Success(val bitmap : Bitmap?) : CameraState()
    data class Error(val msg : String?) : CameraState()
}