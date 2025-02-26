package com.hackaton.sustaina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hackaton.sustaina.LoginPage
import com.hackaton.sustaina.RegisterPage
import com.hackaton.sustaina.ui.aboutissue.AboutIssue
import com.hackaton.sustaina.ui.camera.CameraScreen
import com.hackaton.sustaina.ui.camera.VerifyCameraPermissions

sealed class Routes(val route: String) {
    data object Login :  Routes("Login")
    data object Register : Routes("Register")
    data object Camera : Routes("Camera")
    data object VerifyCameraPermissions : Routes("Verify Camera Permissions")
    data object SignOut : Routes("Sign Out")
    data object AboutIssue : Routes("About Issue")
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.VerifyCameraPermissions.route) {
        composable(Routes.Login.route) {
            LoginPage(navController = navController)
        }
        composable(Routes.Register.route) {
            RegisterPage(navController = navController)
        }
        composable(Routes.VerifyCameraPermissions.route) {
            VerifyCameraPermissions(navController = navController)
        }
        composable(Routes.Camera.route) {
            CameraScreen(navController = navController)
        }
        composable(Routes.AboutIssue.route) {
            AboutIssue(navController = navController)
        }
    }
}