package com.hackaton.sustaina.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.time.LocalDateTime
import com.hackaton.sustaina.LoginPage
import com.hackaton.sustaina.RegisterPage
import com.hackaton.sustaina.ui.aboutissue.AboutIssue

sealed class Routes(val route: String) {
    data object Login :  Routes("Login")
    data object Register : Routes("Register")
    data object Landing : Routes("Landing")
    data object SignOut : Routes("Sign Out")
    data object AboutIssue : Routes("About Issue")
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.AboutIssue.route) {
        composable(Routes.Login.route) {
            LoginPage(navController = navController)
        }
        composable(Routes.Register.route) {
            RegisterPage(navController = navController)
        }
        // TODO: pass upcoming campaigns here
        composable(Routes.Landing.route) {
            val events: List<Event> = listOf(Event("UP Hackathon", LocalDateTime.now()), Event("Midterms", LocalDateTime.now()))
            LandingPage(navController = navController, events)
        }
        composable(Routes.AboutIssue.route) {
            AboutIssue(navController = navController)
        }
    }
}