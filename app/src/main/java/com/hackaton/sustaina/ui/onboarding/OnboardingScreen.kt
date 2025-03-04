package com.hackaton.sustaina.ui.onboarding

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hackaton.sustaina.R
import com.hackaton.sustaina.ui.loadingscreen.LoadingScreen
import com.hackaton.sustaina.ui.navigation.Routes
import com.hackaton.sustaina.ui.theme.DarkGreen
import com.hackaton.sustaina.ui.theme.SoftCream
import com.hackaton.sustaina.ui.theme.SustainaTheme

@Composable
fun OnboardingScreenWelcome(navController: NavController, viewModel: OnboardingViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        Log.wtf("OnboardingScreenWelcome", "Current uiState: $uiState")
        if (uiState is OnboardingState.UserExists) {
            Log.wtf("OnboardingScreenWelcome", "Navigating to Landing Page")
            navController.navigate(Routes.Landing.route) {
                popUpTo(Routes.Onboarding.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkUserStatus {}
    }

    when (uiState) {
        is  OnboardingState.Loading,
            OnboardingState.UserExists -> {
                LoadingScreen()
                return
        }
        else -> {}
    }

    Column(
        modifier = Modifier
            .background(SoftCream)
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .drawBehind {
                    val gradient = Brush.radialGradient(
                        colors = listOf(DarkGreen.copy(alpha = 0.5f), SoftCream),
                        center = center,
                        radius = size.minDimension / 1.5f
                    )
                    drawCircle(brush = gradient, radius = size.minDimension / 1.3f, center = center)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.earth),
                contentDescription = "Earth Image",
            )
        }

        Text(
            "Welcome to Sustaina!",
            color = DarkGreen,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 48.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Join us in making a positive impact on the planet.",
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate(Routes.OnboardingDetails.route) }
        ) {
            Text(
                "NEXT",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun OnboardingScreenDetails(navController: NavController, viewModel: OnboardingViewModel = hiltViewModel()) {
    val TAG = "OnboardingScreenDetails"
    var userName by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        Log.wtf(TAG,"Current onboarding state: $uiState")
        if (uiState is OnboardingState.Success) {
            Log.wtf(TAG, "Navigating to Landing Page after onboarding")
            navController.navigate(Routes.Landing.route) {
                popUpTo(Routes.Onboarding.route) { inclusive = true }
            }
        } else if (uiState is OnboardingState.Error) {
            Log.e(TAG, "Error creating new user: ${(uiState as OnboardingState.Error).message}")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_banner_alt),
            contentDescription = "Onboarding Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        )

        Box(modifier = Modifier
            .offset(y = 400.dp)
            .zIndex(0f)
            .background(
                color = SoftCream,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            )
        ) {
            Column(
                modifier = Modifier
                    .background(
                        color = SoftCream,
                        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                    )
                    .fillMaxSize()
                    .offset(y = 100.dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "What should we call you on this journey?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 26.dp)
                )

                Button(
                    onClick = { viewModel.createUser(userName) }
                ) {
                    Text(
                        "GET STARTED",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SustainaTheme { OnboardingScreenDetails(rememberNavController()) }
}