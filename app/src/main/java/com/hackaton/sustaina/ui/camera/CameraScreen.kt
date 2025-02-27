package com.hackaton.sustaina.ui.camera

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.ContactsContract.Directory
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.hackaton.sustaina.R
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

lateinit var cameraExecutor: Executor
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VerifyCameraPermissions(navController: NavController) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val cameraHasPermission = cameraPermissionState.status.isGranted
    val cameraOnRequestPermission = cameraPermissionState::launchPermissionRequest

//    val writePermissionState = rememberPermissionState(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    val writeHasPermission = writePermissionState.status.isGranted
//    val writeOnRequestPermission = writePermissionState::launchPermissionRequest

    if (cameraHasPermission) {
        CameraScreen(navController)
    } else {
        NoPermissionScreen(navController = navController,
            cameraOnRequestPermission = cameraOnRequestPermission)
    }
}

@Composable
fun NoPermissionScreen(navController: NavController,
                       cameraOnRequestPermission: () -> Unit) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Grant thyself perms!", fontSize = 50.sp)
        Button(onClick = cameraOnRequestPermission, modifier =  Modifier.size(150.dp, 50.dp)) {
           Text("Camera")
        }
    }
}

@Composable
fun CameraScreen(
    navController: NavController
) {

    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (row, cameraPreviewContainer) = createRefs()

        Box (
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(cameraPreviewContainer) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            CameraPreview(imageCapture = imageCapture)
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(ContextCompat.getColor(LocalContext.current, R.color.translucent_black)))
                .height(150.dp)
                .padding(0.dp, 0.dp, 0.dp, 40.dp)
                .constrainAs(row) {
                    bottom.linkTo(parent.bottom)
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                .clickable { capturePhoto(imageCapture = imageCapture, context = context) }
            ) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(100.dp))
                )
                Icon(
                    painter = painterResource(R.drawable.icon_camera),
                    contentDescription = "Camera Icon",
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }
    }
}

@Composable
fun CameraPreview(modifier: Modifier = Modifier, imageCapture: ImageCapture) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = { context ->
            val previewView = PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                setBackgroundColor(android.graphics.Color.BLACK)
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_START
            }

            val previewUseCase = Preview.Builder().build()
            previewUseCase.surfaceProvider = previewView.surfaceProvider

            coroutineScope.launch {
                val cameraProvider = context.cameraProvider()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    previewUseCase,
                    imageCapture
                )
            }

            previewView
        }
    )
}

suspend fun Context.cameraProvider() : ProcessCameraProvider = suspendCoroutine { continuation ->
    val listenableFuture = ProcessCameraProvider.getInstance(this)
    listenableFuture.addListener({
        continuation.resume(listenableFuture.get())
    }, ContextCompat.getMainExecutor(this))
}

private fun capturePhoto(imageCapture: ImageCapture, context: Context) {
    cameraExecutor = Executors.newSingleThreadExecutor()
    val currentTime = System.currentTimeMillis()
    val name = "IMG_$currentTime.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }
    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    val outputFileOption = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver, uri, contentValues
    ).build()

    imageCapture.takePicture(outputFileOption, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            Log.e("Camera", "The saved uri is ${outputFileResults.savedUri}")
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("Camera", exception.toString())
        }

    })
}