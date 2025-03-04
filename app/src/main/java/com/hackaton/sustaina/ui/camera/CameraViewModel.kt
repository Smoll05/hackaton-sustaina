package com.hackaton.sustaina.ui.camera

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.sustaina.data.api.RetrofitClient
import com.hackaton.sustaina.data.camera.CameraRepository
import com.hackaton.sustaina.data.ml.TrashDetectorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraRepository: CameraRepository,
    private val trashDetectorRepository: TrashDetectorRepository
) : ViewModel() {

    private val _cameraState : MutableStateFlow<CameraState> = MutableStateFlow(CameraState.Idle)
    private val cameraExecutor : Executor = Executors.newSingleThreadExecutor()

    val cameraState: StateFlow<CameraState> = _cameraState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.Q)
    fun capturePhoto(imageCapture: ImageCapture, context: Context) {
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
                        val outputUri = outputFileResults.savedUri
                        Log.e("Camera", "The saved uri is $outputUri")

                        outputUri?.let {
                            val file = uriToFile(it, context)

                            _cameraState.update { CameraState.Success(file) }
                            cameraRepository.lastFileSaved = file

                            if (file != null) {
                                Log.d("API", "Image file created at: ${file.absolutePath}")

                                uploadImage(file)
                            } else {
                                Log.e("API", "Failed to get image file")
                            }
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("Camera", "$exception: ${exception.cause}")
                        _cameraState.update { CameraState.Error(exception.message) }
                    }
                })
        }
    }

    fun uploadImage(file: File) {
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val apiService = RetrofitClient.instance
        val call = apiService.uploadImage(body)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseString = response.body()?.string()
                    Log.d("API_RESPONSE", responseString.orEmpty())

                    val detections = parseDetections(responseString)
                    Log.d("Detections", detections.toString())
                } else {
                    Log.e("API_ERROR", "Response not successful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API_ERROR", "Request failed: ${t.message}")
            }
        })
    }


    private fun uriToFile(uri: Uri, context: Context): File? {
        val file = File(context.cacheDir, "captured_image.jpg")

        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun parseDetections(response: String?): Map<String, Int> {
        if (response.isNullOrEmpty()) return emptyMap()

        val detections = mutableMapOf<String, Int>()
        val jsonObject = JSONObject(response)
        val detectionObject = jsonObject.getJSONObject("detections")

        detectionObject.keys().forEach { key ->
            detections[key] = detectionObject.getInt(key)
        }

        return detections
    }
}
