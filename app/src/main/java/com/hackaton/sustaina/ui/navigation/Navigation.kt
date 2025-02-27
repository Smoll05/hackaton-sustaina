package com.hackaton.sustaina.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hackaton.sustaina.LoginPage
import com.hackaton.sustaina.RegisterPage
import com.hackaton.sustaina.ui.aboutissue.AboutIssue
import com.hackaton.sustaina.ui.camera.CameraScreen
import com.hackaton.sustaina.ui.landing.LandingPage

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
        composable(Routes.AboutIssue.route) {
        // TODO: pass upcoming campaigns here (IDs)
        composable(Routes.Landing.route) {
            var events: List<String> = listOf("UP12345", "MDTM12345")
//            events = emptyList()
            LandingPage(navController = navController, upcomingCampaigns = events)
        }
        composable(
            route = Routes.AboutIssue.route,
            arguments = listOf(navArgument("campaignId") { type = NavType.StringType })
        ) {
            AboutIssue(navController = navController)
        }
    }
}