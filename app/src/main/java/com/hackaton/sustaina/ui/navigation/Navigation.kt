package com.hackaton.sustaina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hackaton.sustaina.LoginPage
import com.hackaton.sustaina.RegisterPage
import com.hackaton.sustaina.ui.aboutissue.AboutIssue
import com.hackaton.sustaina.ui.camera.CameraScreen
import com.hackaton.sustaina.ui.landing.LandingPageScreen

sealed class Routes(val route: String) {
    data object Login :  Routes("Login")
    data object Register : Routes("Register")
    data object Camera : Routes("Camera")
    data object Landing : Routes("Landing")
    data object SignOut : Routes("Sign Out")
    data object AboutIssue : Routes("AboutIssue/{campaignId}")
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Login.route) {
        composable(Routes.Login.route) {
            LoginPage(navController = navController)
        }
        composable(Routes.Register.route) {
            RegisterPage(navController = navController)
        }
        composable(Routes.Camera.route) {
            CameraScreen(navController = navController)
        }
        composable(Routes.Landing.route) {
            LandingPageScreen(navController = navController)
        }
        composable(
            route = Routes.AboutIssue.route,
            arguments = listOf(navArgument("campaignId") { type = NavType.StringType })
        ) {
            AboutIssue(navController = navController)
        }
    }
}