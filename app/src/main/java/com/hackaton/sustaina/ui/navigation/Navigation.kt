package com.hackaton.sustaina.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hackaton.sustaina.LoginPage
import com.hackaton.sustaina.RegisterPage
import com.hackaton.sustaina.ui.aboutissue.AboutIssue
import com.hackaton.sustaina.ui.camera.CameraScreen
import com.hackaton.sustaina.ui.landing.LandingPage

sealed class Routes(val route: String) {
    data object Landing : Routes("Landing")
    data object Login : Routes("Login")
    data object Register : Routes("Register")
    data object Camera : Routes("Camera")
    data object SignOut : Routes("Sign Out")
    data object AboutIssue : Routes("About Issue")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
 fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(Routes.Landing.route, Routes.Camera.route, Routes.AboutIssue.route)) {
                BottomNavigationBar(navController)
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Routes.Login.route
            ) {
                composable(Routes.AboutIssue.route) {
                    LandingPage(navController = navController)
                }
                composable(Routes.Camera.route) {
                    CameraScreen(navController = navController)
                }
                composable(Routes.Landing.route) {
                    AboutIssue(navController = navController)
                }

                composable(Routes.Login.route) {
                    LoginPage(navController = navController)
                }
                composable(Routes.Register.route) {
                    RegisterPage(navController = navController)
                }
            }
        }
    }
}
