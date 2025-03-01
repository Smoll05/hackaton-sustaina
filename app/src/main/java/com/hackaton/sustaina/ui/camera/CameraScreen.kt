package com.hackaton.sustaina.ui.camera

import android.content.Context
import android.os.Build
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VerifyCameraPermissions(navController: NavController) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val cameraHasPermission = cameraPermissionState.status.isGranted
    val cameraOnRequestPermission = cameraPermissionState::launchPermissionRequest

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

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CameraScreen(
    navController: NavController,
    cameraViewModel: CameraViewModel = hiltViewModel()
) {

    val cameraState by cameraViewModel.cameraState.collectAsState()

    if (cameraState is CameraState.Success) {
        navController.popBackStack()
    }

    val imageCapture = remember { ImageCapture.Builder().build() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var flashAlpha by remember { mutableFloatStateOf(0f) }
    val animatedAlpha by animateFloatAsState(
        targetValue = flashAlpha,
        animationSpec = tween(durationMillis = 100)
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp)
    ) {
        val (previewContainer, captureButton) = createRefs()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .constrainAs(previewContainer) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Box(
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(30.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                CameraPreview(imageCapture = imageCapture)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = animatedAlpha))
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .constrainAs(captureButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .offset(y = (-50).dp)
                    .size(70.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(100.dp))
                    .clickable {
                        cameraViewModel.capturePhoto(imageCapture = imageCapture, context = context)
                        coroutineScope.launch {
                            flashAlpha = 0.8f
                            delay(50)
                            flashAlpha = 0f
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(100.dp))
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