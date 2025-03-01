package com.hackaton.sustaina.ui.camera

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(

) : ViewModel() {

    private val _cameraState : MutableStateFlow<CameraState> = MutableStateFlow(CameraState.Idle)
    private val cameraExecutor : ExecutorService = Executors.newSingleThreadExecutor()

    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun capturePhoto(imageCapture: ImageCapture, context: Context) {
        viewModelScope.launch {
            _cameraState.update { CameraState.Loading }
            val currentTime = System.currentTimeMillis()
            val currentTimeInSeconds = (currentTime / 1000).toInt()
            val name = "IMG_$currentTime.jpg"
            val mimeType = "image/jpeg"
            val relativePath = "Pictures/Sustaina"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.DATE_TAKEN, currentTimeInSeconds)
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            }

            val uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)!!

            val outputFileOption = ImageCapture.OutputFileOptions.Builder(
                context.contentResolver, uri, contentValues
            ).build()

            imageCapture.takePicture(
                outputFileOption,
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        Log.e("Camera", "The saved uri is ${outputFileResults.savedUri}")
                        _cameraState.update { CameraState.Success(outputFileResults.savedUri?.toFile()) }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("Camera", "$exception: ${exception.cause}")
                        _cameraState.update { CameraState.Error(exception.message) }
                    }
                })
        }
    }
}
