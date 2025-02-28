package com.hackaton.sustaina.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hackaton.sustaina.LoginPage
import com.hackaton.sustaina.RegisterPage
import com.hackaton.sustaina.ui.aboutissue.AboutIssue
import com.hackaton.sustaina.ui.camera.CameraScreen
import com.hackaton.sustaina.ui.landing.LandingPageScreen
import com.hackaton.sustaina.ui.profile.ProfilePage
import com.hackaton.sustaina.ui.map.MapScreen

sealed class Routes(val route: String) {
    data object Landing : Routes("Landing")
    data object Login : Routes("Login")
    data object Register : Routes("Register")
    data object Camera : Routes("Camera")
    data object SignOut : Routes("Sign Out")
    data object Map : Routes("Map")
    data object AboutIssue : Routes("About Issue")
    data object Profile : Routes("Profile")
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
 fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(
                    Routes.Landing.route,
                    Routes.Camera.route,
                    Routes.Map.route)) {
                BottomNavigationBar(navController)
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Routes.Login.route
            ) {
                // Bottom NavBar Screens
                // TODO: pass upcoming campaigns here (IDs)
                composable(Routes.Landing.route) {
                    var events: List<String> = listOf("UP12345", "MDTM12345")
//            events = emptyList()
                    LandingPageScreen(navController = navController, upcomingCampaigns = events)
                }
                composable(Routes.Camera.route) {
                    CameraScreen(navController = navController)
                }
                composable(Routes.Map.route) {
                    MapScreen(navController = navController)
                }
                composable(Routes.AboutIssue.route) {
                    AboutIssue(navController = navController)
                }


                composable(Routes.Login.route) {
                    LoginPage(navController = navController)
                }
                composable(Routes.Register.route) {
                    RegisterPage(navController = navController)
                }
                composable(Routes.Profile.route) {
                    ProfilePage(navController = navController)
                }

            }
        }
    }
}