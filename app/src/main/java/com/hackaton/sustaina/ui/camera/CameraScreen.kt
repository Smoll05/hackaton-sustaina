package com.hackaton.sustaina.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.hackaton.sustaina.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VerifyCameraPermissions(navController: NavController) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val hasPermission = cameraPermissionState.status.isGranted
    val onRequestPermission = cameraPermissionState::launchPermissionRequest

    if (hasPermission) {
        CameraScreen(navController)
    } else {
        NoPermissionScreen(navController = navController, onRequestPermission = onRequestPermission)
    }
}

@Composable
fun NoPermissionScreen(navController: NavController, onRequestPermission: () -> Unit) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onRequestPermission, modifier =  Modifier.size(150.dp, 50.dp)) {
           Text("Grant Permission")
        }
    }
}

@Composable
fun CameraScreen(
    navController: NavController
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

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

                    val listenableFuture = ProcessCameraProvider.getInstance(context)
                    listenableFuture.addListener({
                        val cameraProvider = listenableFuture.get()
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, previewUseCase)
                    }, ContextCompat.getMainExecutor(context))

                    previewView
                }
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(ContextCompat.getColor(context, R.color.translucent_black)))
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
                .clickable { capturePhoto(context, cameraController) }
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

private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController
) {
    val mainExecutor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
    })
}