package com.hackaton.sustaina.ui.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
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
import kotlinx.coroutines.delay

sealed class Routes(val route: String) {
    data object Landing : Routes("Landing")
    data object Login : Routes("Login")
    data object Register : Routes("Register")
    data object Camera : Routes("Camera")
    data object Map : Routes("Map")
    data object AboutIssue : Routes("AboutIssue/{campaignId}")
    data object Profile : Routes("Profile")
}

@SuppressLint("ContextCastToActivity")
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = "splash"
        ) {
            composable("splash") {
                SplashScreen(navController)
            }
            composable("main") {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.white))
                ) {
                    Navigation()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.LeafyGreen)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_sustaina),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Sustaina",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(2000)
        (context as Activity).window.navigationBarColor = ContextCompat.getColor(context, R.color.white)
        navController.navigate("main") {
            popUpTo("splash") { inclusive = true }
        }
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

                composable(Routes.Camera.route) { VerifyCameraPermissions(navController = navController) }
                composable(Routes.Map.route) { MapScreen(navController = navController) }

                // Other Screens
                composable(Routes.Login.route) { LoginPage(navController = navController) }
                composable(Routes.Register.route) { RegisterPage(navController = navController) }
                composable(Routes.Profile.route) { ProfileScreen(navController = navController) }

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
