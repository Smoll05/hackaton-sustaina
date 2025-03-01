package com.hackaton.sustaina.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hackaton.sustaina.LoginPage
import com.hackaton.sustaina.R
import com.hackaton.sustaina.RegisterPage
import com.hackaton.sustaina.ui.campaigninfo.CampaignInfoScreen
import com.hackaton.sustaina.ui.camera.VerifyCameraPermissions
import com.hackaton.sustaina.ui.landing.LandingPageScreen
import com.hackaton.sustaina.ui.map.MapScreen
import com.hackaton.sustaina.ui.profile.ProfileScreen

sealed class Routes(val route: String) {
    data object Landing : Routes("Landing")
    data object Login : Routes("Login")
    data object Register : Routes("Register")
    data object Camera : Routes("Camera")
    data object SignOut : Routes("Sign Out")
    data object Map : Routes("Map")
    data object AboutIssue : Routes("AboutIssue/{campaignId}")
    data object Profile : Routes("Profile")
}

// Revert back window color to white
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen() {
    Box(modifier = Modifier.fillMaxSize().background(color = colorResource(id = R.color.white))) {
        Navigation()
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
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
                composable(Routes.Landing.route) {
                    BackHandler { /* do nothing */ }
                    LandingPageScreen(navController = navController)
                }
                composable(Routes.Camera.route) {
                    VerifyCameraPermissions(navController = navController)
                }
                composable(Routes.Map.route) {
                    MapScreen(navController = navController)
                }

                composable(Routes.Login.route) {
                    LoginPage(navController = navController)
                }
                composable(Routes.Register.route) {
                    RegisterPage(navController = navController)
                }
                composable(Routes.Profile.route) {
                    ProfileScreen(navController = navController)
                }
                composable(
                    route = Routes.AboutIssue.route,
                    arguments = listOf(navArgument("campaignId") { type = NavType.StringType })
                ) {
                    CampaignInfoScreen(navController = navController)
                }
            }
        }
    }
}